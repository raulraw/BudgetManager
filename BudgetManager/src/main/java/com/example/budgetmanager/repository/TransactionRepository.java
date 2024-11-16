package com.example.budgetmanager.repository;

import com.example.budgetmanager.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Poți adăuga metode personalizate pentru a filtra tranzacțiile după utilizator sau categorie
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByCategoryId(Long categoryId);
}
