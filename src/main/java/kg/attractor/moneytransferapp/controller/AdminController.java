package kg.attractor.moneytransferapp.controller;

import kg.attractor.moneytransferapp.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final WalletService wallets;
    
    @GetMapping
    public String list(@RequestParam(defaultValue = "false")
    boolean pending, Model model) {
        model.addAttribute("transactions", wallets.adminTransactions(pending));
        model.addAttribute("pendingOnly", pending);
        return "admin";
    }
    
    @GetMapping("/transactions/{id}")
    public String detail(@PathVariable
    Long id, Principal p, Model model) {
        model.addAttribute("tx", wallets.transaction(id, p.getName(), true));
        model.addAttribute("adminView", true);
        return "transaction";
    }
    
    @PostMapping("/transactions/{id}/approve")
    public String approve(@PathVariable
    Long id, Principal p, RedirectAttributes flash) {
        flash.addFlashAttribute("notice", "notice." + wallets.review(id, p.getName(), true));
        return "redirect:/admin/transactions/" + id;
    }
    
    @PostMapping("/transactions/{id}/reject")
    public String reject(@PathVariable
    Long id, Principal p, RedirectAttributes flash) {
        flash.addFlashAttribute("notice", "notice." + wallets.review(id, p.getName(), false));
        return "redirect:/admin/transactions/" + id;
    }
}
