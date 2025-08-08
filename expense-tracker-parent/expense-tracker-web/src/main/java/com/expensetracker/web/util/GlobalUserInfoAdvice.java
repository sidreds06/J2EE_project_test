package com.expensetracker.web.util;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserInfoAdvice {

    @ModelAttribute
    public void addUserInfo(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            model.addAttribute("loggedInName", principal.getAttribute("name"));
            model.addAttribute("loggedInEmail", principal.getAttribute("email"));
        }
    }
}
