package kg.attractor.moneytransferapp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.context.i18n.LocaleContextHolder;

@ControllerAdvice
public class CommonAdvice {
    
    @ModelAttribute
    public void common(Authentication authentication, Model model) {
        boolean authenticated = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
        model.addAttribute("signedIn", authenticated);
        model.addAttribute("currentUsername", authenticated ? authentication.getName() : "");
        model.addAttribute("isAdmin", authenticated && authentication.getAuthorities().stream().anyMatch((a)->a.getAuthority().equals("ROLE_ADMIN")));
        model.addAttribute("currentLanguage", LocaleContextHolder.getLocale().getLanguage());
    }
}
