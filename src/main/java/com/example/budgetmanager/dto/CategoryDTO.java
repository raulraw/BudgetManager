package com.example.budgetmanager.dto;

public class CategoryDTO {
    private String name;

    // Constructori, Getters și Setters
    public CategoryDTO() {}

    public CategoryDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
