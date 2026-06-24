package com.example.hoodle.Service;

import com.example.hoodle.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtGenerator {


    @Value("${jwt.secret}")
    private String secret;

    @Value("${app.jwt.token.message}")
    private String message;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiration.ms}")
    private Long expirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Map<String,String> generateToken(User userLogin) {
        String token = Jwts.builder().subject(userLogin.getUserName()).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+ 3600000)).signWith(getSigningKey()).compact();
        Map<String,String> response = new HashMap<>();
        response.put("token",token);
        response.put("message", message);
        return response;
    }

    public String generateJwtToken(User user, List<String> roles) {
        return  Jwts.builder().subject(user.getId().toString())
                .claim("userEmail", user.getEmailId())
                .claim("tenantId", user.getTenant().getId())
                .claim("roles", roles)
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ expirationMs))
                .signWith(getSigningKey()).compact();
    }

    public String extractUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false; // Token is expired or tampered with
        }
    }
}
