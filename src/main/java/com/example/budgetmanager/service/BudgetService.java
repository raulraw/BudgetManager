package com.example.budgetmanager.service;

import com.example.budgetmanager.entity.User;
import com.example.budgetmanager.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    private final UserRepository userRepository;

    public BudgetService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Metoda pentru actualizarea bugetului după adăugarea unei cheltuieli
    public void updateBudgetAfterExpense(Long userId, BigDecimal expenseAmount) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getBudget() != null) {
                BigDecimal remainingAmount = user.getBudget().getRemainingAmount();
                // Scădem suma cheltuielii din bugetul rămas
                user.getBudget().setRemainingAmount(remainingAmount.subtract(expenseAmount));
                userRepository.save(user); // Salvăm utilizatorul cu bugetul actualizat
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
                user.getBudget().setRemainingAmount(user.getBudget().getAmount());
                userRepository.save(user); // Salvează modificările
            }
        }
    }
}
