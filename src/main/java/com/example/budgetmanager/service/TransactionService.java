package com.example.budgetmanager.service;

import com.example.budgetmanager.entity.Transaction;
import com.example.budgetmanager.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Salvarea unei tranzacții
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // Obținerea tranzacțiilor pentru un utilizator
    public List<Transaction> getTransactionsByUser(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    // Obținerea tranzacțiilor între două date
    public List<Transaction> getTransactionsBetweenDates(Long userId, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }

    // Ștergerea unei tranzacții
    public boolean deleteTransaction(Long transactionId) {
        if (transactionRepository.existsById(transactionId)) {
            transactionRepository.deleteById(transactionId);
            return true;
        }
        return false;
    }
}
