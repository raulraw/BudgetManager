package com.example.budgetmanager.service;

import com.example.budgetmanager.entity.Budget;
import com.example.budgetmanager.entity.User;
import com.example.budgetmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final ExpenseService expenseService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BudgetService budgetService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ExpenseService expenseService, BudgetService budgetService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.expenseService = expenseService;
        this.budgetService = budgetService;
    }

    // Găsește toți utilizatorii
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // Salvează un utilizator nou
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Criptăm parola
        return userRepository.save(user);
    }

    // Găsește un utilizator după ID
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Găsește un utilizator după Username
    public Optional<User> findUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username)); // Returnează un Optional
    }

    // Actualizează un utilizator existent
    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            user.setRole(userDetails.getRole());
            return userRepository.save(user);
        });
    }

    // Șterge un utilizator după ID
    public Optional<Boolean> deleteUser(Long id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return true;
        });
    }

    //setam un buget pentru utilizator
    public Budget setBudgetForUser(Long userId, Budget newBudget) {
        // Găsește utilizatorul
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Găsește bugetul existent
        Budget currentBudget = user.getBudget();
        if (currentBudget == null) {
            currentBudget = new Budget();
            user.setBudget(currentBudget);
        }

        // Setează noile valori ale bugetului
        currentBudget.setAmount(newBudget.getAmount());
        currentBudget.setResetDay(newBudget.getResetDay());

        // Calculează totalul cheltuielilor pentru perioada respectivă
        LocalDate today = LocalDate.now();
        int resetDay = newBudget.getResetDay();
        LocalDate periodStart = today.withDayOfMonth(resetDay).isAfter(today)
                ? today.withDayOfMonth(resetDay).minusMonths(1)
                : today.withDayOfMonth(resetDay);
        LocalDate periodEnd = periodStart.plusMonths(1);

        // Folosește ExpenseService pentru a calcula totalul cheltuielilor
        BigDecimal totalExpenses = expenseService.calculateTotalExpenses(userId, periodStart, periodEnd);

        // Recalculăm remainingAmount
        BigDecimal remainingAmount = newBudget.getAmount().subtract(totalExpenses);
        currentBudget.setRemainingAmount(remainingAmount);

        // Persistă modificările
        userRepository.save(user);

        return currentBudget;
    }


    public Optional<User> updateUserName(Long userId, String newName) {
        return userRepository.findById(userId).map(user -> {
            user.setFullName(newName);
            return userRepository.save(user);
        });
    }
}
