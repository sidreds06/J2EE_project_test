package com.expensetracker.service;

import com.expensetracker.model.Expense;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseService {

    List<Expense> getUserExpenses(String email);

    List<Expense> getUserExpensesByCategory(String email, String category);

    List<Expense> getUserExpensesByCurrency(String email, String currency);

    List<Expense> getUserExpensesByDateRange(String email, LocalDate start, LocalDate end);

    Expense getExpenseById(Long id);

    Expense saveExpense(Expense expense);

    void deleteExpense(Long id);
    
    Page<Expense> getUserExpenses(String email, Pageable pageable);

}
