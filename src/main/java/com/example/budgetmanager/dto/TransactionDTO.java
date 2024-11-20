package com.example.budgetmanager.dto;

public class TransactionDTO {
    private Long userId;
    private CategoryDTO category;
    private double amount;
    private String description;
    private String date;

    // Constructori, Getters și Setters
    public TransactionDTO() {}

    public TransactionDTO(Long userId, CategoryDTO category, double amount, String description, String date) {
        this.userId = userId;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
