package kg.attractor.moneytransferapp.controller;

import kg.attractor.moneytransferapp.service.*;
import kg.attractor.moneytransferapp.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.format.annotation.DateTimeFormat;
import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class WalletController {
    private final WalletService wallets;
    private final UserService users;
    
    private void homeData(Principal principal, Model model) {
        model.addAttribute("rates", wallets.rates());
        if (principal != null) {
            model.addAttribute("accounts", wallets.accounts(principal.getName()));
            model.addAttribute("transactions", wallets.history(principal.getName(), null, null, null));
        }
    }
    
    private void transferData(Principal principal, Model model) {
        model.addAttribute("accounts", wallets.accounts(principal.getName()));
        model.addAttribute("rates", wallets.rates());
    }
    
    private void paymentData(Principal principal, Model model) {
        model.addAttribute("accounts", wallets.accounts(principal.getName()));
        model.addAttribute("providers", wallets.providers());
    }
    
    @GetMapping("/")
    public String home(Principal principal, Model model) {
        homeData(principal, model);
        model.addAttribute("topUpDto", new TopUpFormDto());
        return "home";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registrationDto", new UserRegistrationDto());
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@Valid
    @ModelAttribute("registrationDto")
    UserRegistrationDto dto, BindingResult result, Model model) {
        if (result.hasErrors()) return "register";
        users.register(dto);
        return "redirect:/login?registered";
    }
    
    @PostMapping("/accounts")
    public String createAccount(Principal principal, @RequestParam
    String currency, RedirectAttributes flash) {
        wallets.createAccount(principal.getName(), currency);
        flash.addFlashAttribute("notice", "notice.accountCreated");
        return "redirect:/";
    }
    
    @PostMapping("/topup")
    public String topUp(Principal principal, @Valid
    @ModelAttribute("topUpDto")
    TopUpFormDto dto, BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            homeData(principal, model);
            return "home";
        }
        flash.addFlashAttribute("notice", "notice." + wallets.topUp(dto.getAccountId(), dto.getAmount()));
        return "redirect:/";
    }
    
    @GetMapping("/transfer")
    public String transfer(Principal principal, Model model) {
        transferData(principal, model);
        model.addAttribute("transferDto", new TransferFormDto());
        return "transfer";
    }
    
    @PostMapping("/transfer")
    public String transfer(Principal principal, @Valid
    @ModelAttribute("transferDto")
    TransferFormDto dto, BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            transferData(principal, model);
            return "transfer";
        }
        flash.addFlashAttribute("notice", "notice." + wallets.transfer(principal.getName(), dto.getSourceId(), dto.getTargetId(), dto.getAmount()));
        return "redirect:/";
    }
    
    @GetMapping("/payment")
    public String payment(Principal principal, Model model) {
        paymentData(principal, model);
        model.addAttribute("paymentDto", new PaymentFormDto());
        return "payment";
    }
    
    @PostMapping("/payment")
    public String payment(Principal principal, @Valid
    @ModelAttribute("paymentDto")
    PaymentFormDto dto, BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            paymentData(principal, model);
            return "payment";
        }
        flash.addFlashAttribute("notice", "notice." + wallets.pay(principal.getName(), dto.getSourceId(), dto.getProviderId(), dto.getReference(), dto.getAmount()));
        return "redirect:/";
    }
    
    @GetMapping("/history")
    public String history(Principal principal, Model model, @RequestParam(required = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate from, @RequestParam(required = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate to, @RequestParam(defaultValue = "date")
    String sort) {
        model.addAttribute("transactions", wallets.history(principal.getName(), from, to, sort));
        model.addAttribute("fromValue", from == null ? "" : from.toString());
        model.addAttribute("toValue", to == null ? "" : to.toString());
        model.addAttribute("sortValue", sort);
        return "history";
    }
    
    @GetMapping("/transactions/{id}")
    public String transaction(Principal principal, @PathVariable
    Long id, Model model) {
        model.addAttribute("tx", wallets.transaction(id, principal.getName(), false));
        return "transaction";
    }
}
