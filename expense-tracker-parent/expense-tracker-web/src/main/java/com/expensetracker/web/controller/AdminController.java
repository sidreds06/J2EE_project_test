package com.expensetracker.web.controller;

import com.expensetracker.model.Expense;
import com.expensetracker.auth.entity.AuthUser;
import com.expensetracker.auth.repository.AuthUserRepository;
import com.expensetracker.service.ExpenseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // Restrict access to admin only
public class AdminController {

    @Autowired
    private AuthUserRepository userRepo;

    @Autowired
    private ExpenseService expenseService;

    // Admin dashboard - shows all users
    @GetMapping("/users")
    public String dashboard(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AuthUser> usersPage;

        if (keyword != null && !keyword.isBlank()) {
            usersPage = userRepo.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, pageable);
        } else {
            usersPage = userRepo.findAll(pageable);
        }

        model.addAttribute("usersPage", usersPage);
        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "admin/users";
    }


    // Delete a user by email
    @GetMapping("/delete/{email}")
    public String deleteUser(@PathVariable String email) {
        userRepo.deleteById(email);
        return "redirect:/admin/users";
    }

    // View a specific user's expenses
    @GetMapping("/user/{email}/expenses")
    public String userExpenses(@PathVariable String email, Model model) {
        List<Expense> expenses = expenseService.getUserExpenses(email);
        model.addAttribute("expenses", expenses);
        model.addAttribute("email", email);
        return "admin/user-expenses";
    }
}
