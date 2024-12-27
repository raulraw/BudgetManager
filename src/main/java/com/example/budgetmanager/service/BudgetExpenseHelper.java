package com.example.budgetmanager.service;

import com.example.budgetmanager.entity.Expense;
import com.example.budgetmanager.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BudgetExpenseHelper {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public BudgetExpenseHelper(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    /**
     * Calculează totalul cheltuielilor unui utilizator într-un interval de date.
     *
     * @param userId     ID-ul utilizatorului
     * @param startDate  Data de început a intervalului
     * @param endDate    Data de sfârșit a intervalului
     * @return suma totală a cheltuielilor
     */
    public double calculateTotalExpenses(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        double total = expenses.stream()
                .filter(expense -> !expense.getDate().isBefore(startDate) && !expense.getDate().isAfter(endDate))
                .mapToDouble(Expense::getAmount)
                .sum();
        System.out.println("Total expenses between " + startDate + " and " + endDate + ": " + total);
        return total;
    }
}
