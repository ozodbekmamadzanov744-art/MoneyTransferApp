package kg.attractor.moneytransferapp.service;

import kg.attractor.moneytransferapp.dto.*;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface WalletService {
    
    List<AccountResponseDto> accounts(String username);
    
    List<CurrencyRateResponseDto> rates();
    
    List<ServiceProviderResponseDto> providers();
    
    void createAccount(String username, String code);
    
    String transfer(String username, Long sourceId, Long targetId, BigDecimal value);
    
    String topUp(Long accountId, BigDecimal value);
    
    String pay(String username, Long sourceId, Long providerId, String reference, BigDecimal value);
    
    String review(Long id, String admin, boolean approve);
    
    TransactionResponseDto transaction(Long id, String username, boolean admin);
    
    List<TransactionResponseDto> history(String username, LocalDate from, LocalDate to, String sort);
    
    List<TransactionResponseDto> adminTransactions(boolean pending);
}
