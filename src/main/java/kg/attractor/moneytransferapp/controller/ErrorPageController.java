package kg.attractor.moneytransferapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ErrorPageController {
    
    @GetMapping("/forbidden")
    public String forbidden(Model model, HttpServletResponse response) {
        model.addAttribute("errorKey", "error.forbidden");
        response.setStatus(403);
        return "error";
    }
}
