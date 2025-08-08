package com.expensetracker.web.controller;

import com.expensetracker.model.Expense;
import com.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public String listExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            Model model,
            @AuthenticationPrincipal OAuth2User principal) {

        String email = principal.getAttribute("email");
        Pageable pageable = PageRequest.of(page, size);

        List<Expense> filteredExpenses;

        if (category != null && !category.isEmpty()) {
            filteredExpenses = expenseService.getUserExpensesByCategory(email, category);
        } else if (currency != null && !currency.isEmpty()) {
            filteredExpenses = expenseService.getUserExpensesByCurrency(email, currency);
        } else if (start != null && end != null) {
            filteredExpenses = expenseService.getUserExpensesByDateRange(email, start, end);
        } else {
            filteredExpenses = expenseService.getUserExpenses(email);
        }

        // Convert List to Page manually
        int startIndex = Math.min(page * size, filteredExpenses.size());
        int endIndex = Math.min(startIndex + size, filteredExpenses.size());
        Page<Expense> paged = new PageImpl<>(filteredExpenses.subList(startIndex, endIndex), pageable, filteredExpenses.size());

        model.addAttribute("expenses", paged.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paged.getTotalPages());
        model.addAttribute("category", category);
        model.addAttribute("currency", currency);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "expense/list";
    }


    @GetMapping("/add")
    public String addExpenseForm(Model model) {
        model.addAttribute("expense", new Expense());
        return "expense/form";
    }

    @GetMapping("/edit/{id}")
    public String editExpense(@PathVariable Long id, Model model) {
        model.addAttribute("expense", expenseService.getExpenseById(id));
        return "expense/form";
    }

    @PostMapping("/add")
    public String saveExpense(@ModelAttribute Expense expense,
                              @AuthenticationPrincipal OAuth2User user) {
        expense.setUserEmail(user.getAttribute("email"));
        expenseService.saveExpense(expense);
        return "redirect:/expenses";
    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return "redirect:/expenses";
    }
}