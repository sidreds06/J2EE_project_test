package com.expensetracker.service;

import com.expensetracker.model.Expense;
import com.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepo;

    @Override
    public List<Expense> getUserExpenses(String email) {
        return expenseRepo.findByUserEmail(email);
    }

    @Override
    public List<Expense> getUserExpensesByCategory(String email, String category) {
        return expenseRepo.findByUserEmailAndCategory(email, category);
    }
    
    @Override
    public List<Expense> getUserExpensesByCurrency(String email, String currency) {
        return expenseRepo.findByUserEmailAndCurrency(email, currency);
    }
    

    @Override
    public List<Expense> getUserExpensesByDateRange(String email, LocalDate start, LocalDate end) {
        return expenseRepo.findByUserEmailAndDateBetween(email, start, end);
    }

    @Override
    public Expense getExpenseById(Long id) {
        return expenseRepo.findById(id).orElse(null);
    }

    @Override
    public Expense saveExpense(Expense expense) {
        return expenseRepo.save(expense);
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepo.deleteById(id);
    }
    
    @Override
    public Page<Expense> getUserExpenses(String email, Pageable pageable) {
        return expenseRepo.findByUserEmail(email, pageable);
    }

}
