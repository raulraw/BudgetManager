package com.example.budgetmanager.controller;

import com.example.budgetmanager.entity.Budget;
import com.example.budgetmanager.entity.User;
import com.example.budgetmanager.service.UserService;
import com.example.budgetmanager.util.Roles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. Obține toți utilizatorii
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    // 2. Creează un utilizator nou (implicit USER)
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (user.getFullName() == null || user.getFullName().isEmpty()) {
            return ResponseEntity.badRequest().body("Full Name is required");
        }
        user.setRole(Roles.USER); // Implicit rol USER
        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    // 3. Obține un utilizator după ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Actualizează un utilizator existent
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Șterge un utilizator după ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id)
                .map(deleted -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{userId}/budget")
    public ResponseEntity<Budget> setBudget(@PathVariable Long userId, @RequestBody Budget budget) {
        try {
            Budget updatedBudget = userService.setBudgetForUser(userId, budget);
            return ResponseEntity.ok(updatedBudget);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{userId}/name")
    public ResponseEntity<User> updateUserName(
            @PathVariable Long userId,
            @RequestBody Map<String, String> requestBody) {
        String newName = requestBody.get("name");
        Optional<User> updatedUser = userService.updateUserName(userId, newName);

        return updatedUser.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
