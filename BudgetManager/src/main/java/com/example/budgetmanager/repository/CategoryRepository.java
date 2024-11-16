package com.example.budgetmanager.repository;

import com.example.budgetmanager.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Poți adăuga metode personalizate pentru a căuta categorii după nume
    Category findByName(String name);
}
