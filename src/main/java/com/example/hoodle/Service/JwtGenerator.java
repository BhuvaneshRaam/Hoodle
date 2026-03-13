package com.example.hoodle.Service;

import com.example.hoodle.Entity.UserLogin;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
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

    public Map<String,String> generateToken(UserLogin userLogin) {
        String token = Jwts.builder().subject(userLogin.getUserName()).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+ 3600000)).signWith(getSigningKey()).compact();
        Map<String,String> response = new HashMap<>();
        response.put("token",token);
        response.put("message", message);
        return response;
    }

    public String generateJwtToken(UserLogin userLogin, String role) {
        return  Jwts.builder().subject(userLogin.getUserName())
                .claim("role",role)
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ expirationMs))
                .signWith(getSigningKey()).compact();
    }
}
