package com.example.budgetmanager.service;

import com.example.budgetmanager.entity.Expense;
import com.example.budgetmanager.repository.ExpenseRepository;
import com.example.budgetmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.budgetmanager.entity.User;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository; // Repository pentru User

    // Adăugarea unei noi cheltuieli
    public Expense addExpense(Expense expense) {
        // Verificăm dacă utilizatorul există
        Optional<User> user = userRepository.findById(expense.getUserId());
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }
        return expenseRepository.save(expense);
    }

    // Obținerea tuturor cheltuielilor
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    // Obținerea unei cheltuieli după ID
    public Optional<Expense> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    // Obținerea cheltuielilor unui utilizator
    public List<Expense> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    // Calcularea totalului cheltuielilor
    public Double getTotalExpensesByUserId(Long userId) {
        List<Expense> expenses = getExpensesByUserId(userId);
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    // Actualizarea unei cheltuieli pentru un utilizator specific
    public Expense updateExpense(Long userId, Long id, Expense updatedExpense) {
        Optional<Expense> existingExpense = expenseRepository.findById(id);
        if (existingExpense.isPresent()) {
            Expense expense = existingExpense.get();
            // Verificăm dacă cheltuiala aparține utilizatorului
            if (!expense.getUserId().equals(userId)) {
                throw new RuntimeException("User not authorized to update this expense");
            }
            // Actualizăm câmpurile cheltuielii
            expense.setName(updatedExpense.getName());
            expense.setAmount(updatedExpense.getAmount());
            expense.setCategory(updatedExpense.getCategory());
            expense.setDate(updatedExpense.getDate());
            return expenseRepository.save(expense);
        } else {
            throw new RuntimeException("Expense not found");
        }
    }

    // Ștergerea unei cheltuieli pentru un utilizator specific
    public void deleteExpense(Long userId, Long id) {
        Optional<Expense> existingExpense = expenseRepository.findById(id);
        if (existingExpense.isPresent()) {
            Expense expense = existingExpense.get();
            // Verificăm dacă cheltuiala aparține utilizatorului
            if (!expense.getUserId().equals(userId)) {
                throw new RuntimeException("User not authorized to delete this expense");
            }
            expenseRepository.deleteById(id);
        } else {
            throw new RuntimeException("Expense not found");
        }
    }
}
