package com.example.budgetmanager.service;

import com.example.budgetmanager.entity.User;
import com.example.budgetmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
