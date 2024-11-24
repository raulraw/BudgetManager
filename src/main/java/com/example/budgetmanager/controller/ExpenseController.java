package com.example.budgetmanager.controller;

import com.example.budgetmanager.entity.Expense;
import com.example.budgetmanager.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // Adăugarea unei noi cheltuieli
    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        try {
            Expense createdExpense = expenseService.addExpense(expense);
            return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Dacă utilizatorul nu există
        }
    }

    // Obținerea tuturor cheltuielilor
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        List<Expense> expenses = expenseService.getAllExpenses();
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    // Obținerea unei cheltuieli după ID
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        Optional<Expense> expense = expenseService.getExpenseById(id);
        return expense.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Obținerea cheltuielilor unui utilizator
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Expense>> getExpensesByUserId(@PathVariable Long userId) {
        List<Expense> expenses = expenseService.getExpensesByUserId(userId);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    // Obținerea totalului cheltuielilor unui utilizator
    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Double> getTotalExpensesByUserId(@PathVariable Long userId) {
        Double total = expenseService.getTotalExpensesByUserId(userId);
        return new ResponseEntity<>(total, HttpStatus.OK);
    }

    // Editarea unei cheltuieli pentru un utilizator specific
    @PutMapping("/user/{userId}/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long userId, @PathVariable Long id, @RequestBody Expense expense) {
        try {
            Expense updatedExpense = expenseService.updateExpense(userId, id, expense);
            return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Dacă utilizatorul nu are permisiunea de a edita
        }
    }

    // Ștergerea unei cheltuieli pentru un utilizator specific
    @DeleteMapping("/user/{userId}/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long userId, @PathVariable Long id) {
        try {
            expenseService.deleteExpense(userId, id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Dacă utilizatorul nu are permisiunea de a șterge
        }
    }
}
