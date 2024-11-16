package com.example.budgetmanager.service;

import com.example.budgetmanager.entity.User;
import com.example.budgetmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Metodă pentru a salva un utilizator
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Metodă pentru a găsi un utilizator după username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Metodă pentru a găsi un utilizator după email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
