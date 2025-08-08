package com.expensetracker.repository;

import com.expensetracker.model.Expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Get all expenses for a specific user
    List<Expense> findByUserEmail(String email);

    // Filter by user and category
    List<Expense> findByUserEmailAndCategory(String email, String category);

    // Filter by user and category
    List<Expense> findByUserEmailAndCurrency(String email, String currency);

    // Filter by user and date range
    List<Expense> findByUserEmailAndDateBetween(String email, LocalDate start, LocalDate end);
    
    Page<Expense> findByUserEmail(String email, Pageable pageable);

}
