package com.example.budgetmanager.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Folosește o cheie generată pentru HS256
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Generare Token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Valabilitate: 10 ore
                .signWith(SECRET_KEY)
                .compact();
    }

    // Extrage username-ul din token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Verifică dacă token-ul este valid
    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    // Verifică dacă token-ul a expirat
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Extrage toate datele din token
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder() // Folosește parserBuilder() pentru a evita dependențele învechite
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
