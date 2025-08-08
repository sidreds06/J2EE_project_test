package com.expensetracker.web.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // This will redirect requests from "/" to "/home"
    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }

    // This will serve "home.html" for "/home"
    @GetMapping("/home")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            String name = principal.getAttribute("name");
            String email = principal.getAttribute("email");
            model.addAttribute("name", name);
            model.addAttribute("email", email);
        }
        return "home";
    }

    // This will serve "login.html" for "/login"
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
