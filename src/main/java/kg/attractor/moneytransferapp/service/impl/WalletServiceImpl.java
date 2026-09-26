package kg.attractor.moneytransferapp.service.impl;

import kg.attractor.moneytransferapp.service.WalletService;
import kg.attractor.moneytransferapp.dto.*;
import kg.attractor.moneytransferapp.mapper.*;
import kg.attractor.moneytransferapp.model.*;
import kg.attractor.moneytransferapp.repository.*;
import kg.attractor.moneytransferapp.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WalletServiceImpl implements WalletService {
    private final UserRepository users;
    private final AccountRepository accounts;
    private final CurrencyRateRepository rates;
    private final TransactionRepository transactions;
    private final ServiceProviderRepository providers;
    private final SubscriberRepository subscribers;
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("1000000000.00");
    
    public List<AccountResponseDto> accounts(String username) {
        return accounts.findByUserUsernameOrderByCurrencyAsc(username).stream().map(AccountMapper::toDto).toList();
    }
    
    public List<CurrencyRateResponseDto> rates() {
        return rates.findAll().stream().sorted(Comparator.comparing(CurrencyRate::getCode)).map(CurrencyRateMapper::toDto).toList();
    }
    
    public List<ServiceProviderResponseDto> providers() {
        return providers.findAllByOrderByIdAsc().stream().map(ServiceProviderMapper::toDto).toList();
    }
    
    private BusinessException error(String key) {
        return new BusinessException("error." + key);
    }
    
    private BigDecimal amount(BigDecimal value) {
        if (value == null || value.signum() <= 0 || value.compareTo(MAX_AMOUNT) > 0) throw error("amount");
        try {
            return value.setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException ex) {
            throw error("amount");
        }
    }
    
    private CurrencyRate currency(String code) {
        return rates.findById(code).orElseThrow(()->error("currency"));
    }
    
    private Account account(Long id) {
        if (id == null) throw error("account");
        return accounts.findById(id).orElseThrow(()->error("account"));
    }
    
    private Account own(Long id, String username) {
        Account a = account(id);
        if (!a.getUser().getUsername().equals(username)) throw error("ownership");
        return a;
    }
    
    @Transactional
    public void createAccount(String username, String code) {
        AppUser user = users.findByUsername(username).orElseThrow(()->error("user"));
        currency(code);
        if (accounts.existsByUserIdAndCurrency(user.getId(), code)) throw error("duplicateCurrency");
        if (accounts.countByUserId(user.getId()) >= 3) throw error("accountLimit");
        Account a = new Account();
        a.setUser(user);
        a.setCurrency(code);
        a.setBalance(new BigDecimal("1000.00"));
        accounts.saveAndFlush(a);
        if (a.getId() > 999999) throw error("accountNumbers");
        MoneyTransaction tx = prepare("OPENING", null, a, null, new BigDecimal("1000.00"), code, code);
        tx.setStatus("COMPLETED");
        tx.setProcessedAt(LocalDateTime.now());
        transactions.save(tx);
        log.info("Account created: user={}, account={}, currency={}", username, a.getId(), code);
    }
    
    private MoneyTransaction prepare(String type, Account source, Account target, Subscriber subscriber, BigDecimal value, String from, String to) {
        BigDecimal debit = amount(value);
        BigDecimal fromRate = currency(from).getUnitsPerUsd();
        BigDecimal toRate = currency(to).getUnitsPerUsd();
        BigDecimal converted = debit.multiply(toRate).divide(fromRate, 2, RoundingMode.HALF_UP);
        if (converted.signum() == 0) throw error("tooSmall");
        MoneyTransaction tx = new MoneyTransaction();
        tx.setType(type);
        tx.setSource(source);
        tx.setTarget(target);
        tx.setSubscriber(subscriber);
        tx.setAmount(debit);
        tx.setCurrency(from);
        tx.setCreditedAmount(converted);
        tx.setCreditedCurrency(to);
        tx.setRate(toRate.divide(fromRate, 8, RoundingMode.HALF_UP));
        tx.setCreatedAt(LocalDateTime.now());
        tx.setStatus(debit.compareTo(fromRate.multiply(new BigDecimal("100"))) > 0 ? "PENDING" : "COMPLETED");
        return tx;
    }
    
    private void sufficient(Account a, BigDecimal value) {
        if (a.getBalance().compareTo(value) < 0) throw error("insufficient");
    }
    
    @Transactional
    public String transfer(String username, Long sourceId, Long targetId, BigDecimal value) {
        Account source = own(sourceId, username);
        Account target = account(targetId);
        if (source.getUser().getId().equals(target.getUser().getId())) throw error("selfTransfer");
        MoneyTransaction tx = prepare("TRANSFER", source, target, null, value, source.getCurrency(), target.getCurrency());
        sufficient(source, tx.getAmount());
        return submit(tx);
    }
    
    @Transactional
    public String topUp(Long accountId, BigDecimal value) {
        Account target = account(accountId);
        return submit(prepare("TOPUP", null, target, null, value, target.getCurrency(), target.getCurrency()));
    }
    
    @Transactional
    public String pay(String username, Long sourceId, Long providerId, String reference, BigDecimal value) {
        Account source = own(sourceId, username);
        if (providerId == null || reference == null || reference.length() > 40) throw error("subscriber");
        Subscriber subscriber = subscribers.findByProviderIdAndReference(providerId, reference.trim()).orElseThrow(()->error("subscriber"));
        MoneyTransaction tx = prepare("PAYMENT", source, null, subscriber, value, source.getCurrency(), subscriber.getProvider().getCurrency());
        sufficient(source, tx.getAmount());
        return submit(tx);
    }
    
    private String submit(MoneyTransaction tx) {
        if (tx.getStatus().equals("COMPLETED")) {
            if (!execute(tx)) throw error("insufficient");
        }
        transactions.save(tx);
        log.info("Transaction submitted: id={}, type={}, status={}, amount={}, currency={}", tx.getId(), tx.getType(), tx.getStatus(), tx.getAmount(), tx.getCurrency());
        return tx.getStatus();
    }
    
    private boolean execute(MoneyTransaction tx) {
        Account source = tx.getSource();
        if (source != null && source.getBalance().compareTo(tx.getAmount()) < 0) return false;
        if (source != null) {
            source.setBalance(source.getBalance().subtract(tx.getAmount()));
            accounts.save(source);
        }
        if (tx.getTarget() != null) {
            Account target = tx.getTarget();
            target.setBalance(target.getBalance().add(tx.getCreditedAmount()));
            accounts.save(target);
        }
        if (tx.getSubscriber() != null) {
            Subscriber subscriber = subscribers.findById(tx.getSubscriber().getId()).orElseThrow(()->error("subscriber"));
            subscriber.setBalance(subscriber.getBalance().add(tx.getCreditedAmount()));
            subscribers.save(subscriber);
        }
        tx.setProcessedAt(LocalDateTime.now());
        return true;
    }
    
    @Transactional
    public String review(Long id, String admin, boolean approve) {
        AppUser reviewer = users.findByUsername(admin).orElseThrow(()->error("user"));
        if (!reviewer.getRole().equals("ADMIN")) throw error("forbidden");
        MoneyTransaction tx = transactions.findById(id).orElseThrow(()->error("transaction"));
        if (!tx.getStatus().equals("PENDING")) throw error("alreadyReviewed");
        tx.setReviewedBy(reviewer);
        if (!approve) tx.setStatus("REJECTED"); else if (execute(tx)) tx.setStatus("COMPLETED"); else {
            tx.setStatus("FAILED");
            tx.setFailureKey("error.insufficient");
        }
        tx.setProcessedAt(LocalDateTime.now());
        transactions.save(tx);
        log.info("Transaction reviewed: id={}, admin={}, status={}", id, admin, tx.getStatus());
        return tx.getStatus();
    }
    
    public TransactionResponseDto transaction(Long id, String username, boolean admin) {
        MoneyTransaction tx = transactions.findById(id).orElseThrow(()->error("transaction"));
        boolean owner = (tx.getSource() != null && tx.getSource().getUser().getUsername().equals(username)) || (tx.getTarget() != null && tx.getTarget().getUser().getUsername().equals(username));
        if (!admin && !owner) throw error("ownership");
        return TransactionMapper.toDto(tx);
    }
    
    public List<TransactionResponseDto> history(String username, LocalDate from, LocalDate to, String sort) {
        if (from != null && to != null && from.isAfter(to)) throw error("dates");
        List<MoneyTransaction> result = new ArrayList<>(transactions.findHistory(username).stream().filter((t)->from == null || !t.getCreatedAt().toLocalDate().isBefore(from)).filter((t)->to == null || !t.getCreatedAt().toLocalDate().isAfter(to)).toList());
        if ("currencyAsc".equals(sort) || "currencyDesc".equals(sort)) {
            Comparator<MoneyTransaction> comparator = Comparator.comparing((t)->historyCurrency(t, username));
            if ("currencyDesc".equals(sort)) comparator = comparator.reversed();
            result.sort(comparator);
        }
        return result.stream().map(TransactionMapper::toDto).toList();
    }
    
    private String historyCurrency(MoneyTransaction tx, String username) {
        return tx.getTarget() != null && tx.getTarget().getUser().getUsername().equals(username) ? tx.getCreditedCurrency() : tx.getCurrency();
    }
    
    public List<TransactionResponseDto> adminTransactions(boolean pending) {
        List<MoneyTransaction> result = pending ? transactions.findByStatusOrderByCreatedAtDescIdDesc("PENDING") : transactions.findAllByOrderByCreatedAtDescIdDesc();
        return result.stream().map(TransactionMapper::toDto).toList();
    }
}
