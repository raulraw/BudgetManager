package com.example.budgetmanager.controller;

import com.example.budgetmanager.entity.User;
import com.example.budgetmanager.service.UserService;
import com.example.budgetmanager.util.Roles;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
