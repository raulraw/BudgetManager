package com.example.budgetmanager.repository;

import com.example.budgetmanager.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Poți adăuga metode personalizate
    List<Expense> findByUserId(Long userId);
}
