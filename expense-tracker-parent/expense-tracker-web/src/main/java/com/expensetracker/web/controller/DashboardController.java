package com.expensetracker.web.controller;

import com.expensetracker.model.Expense;
import com.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");

        List<Expense> expenses = expenseService.getUserExpenses(email);

        // Group by category
        Map<String, Double> expensesByCategory = expenses.stream()
            .collect(Collectors.groupingBy(
                Expense::getCategory,
                Collectors.summingDouble(Expense::getAmount)
            ));

        // Group by month (format: yyyy-MM)
        Map<String, Double> expensesByMonth = expenses.stream()
            .collect(Collectors.groupingBy(
                e -> e.getDate().withDayOfMonth(1).toString(),
                TreeMap::new,
                Collectors.summingDouble(Expense::getAmount)
            ));

        model.addAttribute("categoryData", expensesByCategory);
        model.addAttribute("monthData", expensesByMonth);
        return "dashboard";
    }
}
