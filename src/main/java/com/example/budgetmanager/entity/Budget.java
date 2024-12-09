package com.example.budgetmanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount; // Venitul lunar total

    private BigDecimal remainingAmount; // Suma rămasă pentru cheltuieli

    private int resetDay; // Ziua lunii când venitul se resetează

    // Getter și Setter pentru ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter și Setter pentru Amount
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // Getter și Setter pentru RemainingAmount
    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    // Getter și Setter pentru ResetDay
    public int getResetDay() {
        return resetDay;
    }

    public void setResetDay(int resetDay) {
        this.resetDay = resetDay;
    }
}
