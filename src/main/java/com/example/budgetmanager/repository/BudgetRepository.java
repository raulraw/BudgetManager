package com.example.budgetmanager.repository;

import com.example.budgetmanager.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    // Poți adăuga metode personalizate pentru a căuta bugetul
}
