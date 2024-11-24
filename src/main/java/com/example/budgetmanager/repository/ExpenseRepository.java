package com.example.budgetmanager.repository;

import com.example.budgetmanager.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Poți adăuga metode personalizate
    List<Expense> findByUserId(Long userId);
    @Query("SELECT e.category, SUM(e.amount) FROM Expense e WHERE e.userId = :userId GROUP BY e.category")
    List<Object[]> findTotalExpensesByCategory(Long userId);

    @Query("SELECT FUNCTION('MONTH', e.date), SUM(e.amount) FROM Expense e WHERE e.userId = :userId GROUP BY FUNCTION('MONTH', e.date)")
    List<Object[]> findTotalExpensesByMonth(Long userId);
}
