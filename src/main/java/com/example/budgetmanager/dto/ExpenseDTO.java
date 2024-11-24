package com.example.budgetmanager.dto;

import com.example.budgetmanager.enums.CategoryEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class ExpenseDTO {

    private Long userId;

    @NotEmpty(message = "Name is required")
    private String name;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Category is required")
    private CategoryEnum category;

    @NotNull(message = "Date is required")
    private LocalDate date;

    // Constructori
    public ExpenseDTO() {}

    public ExpenseDTO(Long userId, String name, Double amount, CategoryEnum category, LocalDate date) {
        this.userId = userId;
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // Getters și Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
