package com.example.budgetmanager.repository;

import com.example.budgetmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Poți adăuga metode personalizate aici, dacă ai nevoie
    User findByUsername(String username);
    User findByEmail(String email);
}
