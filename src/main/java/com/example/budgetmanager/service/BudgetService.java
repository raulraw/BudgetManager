package com.example.budgetmanager.service;

import com.example.budgetmanager.entity.Budget;
import com.example.budgetmanager.entity.User;
import com.example.budgetmanager.repository.BudgetRepository;
import com.example.budgetmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class BudgetService {

    private final UserRepository userRepository;
    private final BudgetExpenseHelper budgetExpenseHelper;

    @Autowired
    public BudgetService(UserRepository userRepository, BudgetExpenseHelper budgetExpenseHelper) {
        this.userRepository = userRepository;
        this.budgetExpenseHelper = budgetExpenseHelper;
    }

    // Metoda pentru actualizarea bugetului după adăugarea unei cheltuieli
    public void updateBudgetAfterExpense(Long userId, BigDecimal expenseAmount) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getBudget() != null) {
                BigDecimal totalBudget = user.getBudget().getAmount();

                // Calculăm cheltuielile pentru perioada curentă
                LocalDate today = LocalDate.now();
                int resetDay = user.getBudget().getResetDay();
                LocalDate periodStart = today.withDayOfMonth(resetDay).isAfter(today)
                        ? today.withDayOfMonth(resetDay).minusMonths(1)
                        : today.withDayOfMonth(resetDay);
                LocalDate periodEnd = periodStart.plusMonths(1);

                BigDecimal totalExpenses = BigDecimal.valueOf(
                        budgetExpenseHelper.calculateTotalExpenses(userId, periodStart, periodEnd)
                );

                // Recalculăm remainingAmount
                BigDecimal remainingAmount = totalBudget.subtract(totalExpenses).subtract(expenseAmount);

                // Actualizăm bugetul
                user.getBudget().setRemainingAmount(remainingAmount);
                userRepository.save(user);
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // Execută zilnic la miezul nopții
    public void resetBudgets() {
        List<User> users = userRepository.findAll();
        LocalDate today = LocalDate.now();

        for (User user : users) {
            if (user.getBudget() != null && user.getBudget().getResetDay() == today.getDayOfMonth()) {
                // Resetăm remainingAmount la valoarea completă a bugetului
                user.getBudget().setRemainingAmount(user.getBudget().getAmount());
                userRepository.save(user); // Salvează modificările
            }
        }
    }

    public BigDecimal calculateRemainingBudget(Long userId, LocalDate periodStart, LocalDate periodEnd) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getBudget() != null) {
                BigDecimal totalExpenses = BigDecimal.valueOf(
                        budgetExpenseHelper.calculateTotalExpenses(userId, periodStart, periodEnd)
                );
                return user.getBudget().getAmount().subtract(totalExpenses);
            }
        }
        throw new RuntimeException("User or budget not found");
    }
}
