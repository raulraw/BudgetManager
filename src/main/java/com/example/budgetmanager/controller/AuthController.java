package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.AuthRequest;
import com.example.budgetmanager.entity.Expense;
import com.example.budgetmanager.util.JwtUtil;
import com.example.budgetmanager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody AuthRequest authRequest) {
        // Autentificare pe baza username-ului și parolei
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()) // Folosim username-ul
        );

        // Obținem detaliile utilizatorului pe baza username-ului
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        // Generăm token-ul JWT
        String token = jwtUtil.generateToken(userDetails.getUsername());

        // Obținem ID-ul utilizatorului pe baza username-ului
        Long userId = userService.findUserByUsername(authRequest.getUsername()).get().getId();

        String fullName = userService.findUserByUsername(authRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getFullName();

        // Returnăm atât token-ul cât și userId-ul
        return Map.of(
                "token", token,
                "userId", userId,
                "fullName", fullName
        );
    }
}
