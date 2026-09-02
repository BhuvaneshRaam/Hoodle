package com.example.hoodle.controller;

import com.example.hoodle.DTO.InitResponse;
import com.example.hoodle.DTO.LoginRequest;
import com.example.hoodle.DTO.TenantRegistrationRequest;
import com.example.hoodle.Entity.User;
import com.example.hoodle.Exception.CustomException;
import com.example.hoodle.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<?> userRegisration(@RequestBody TenantRegistrationRequest request) throws CustomException{
        userService.registerTenantAndAdmin(request);
        return ResponseEntity.ok(Map.of("message", "Tenant and Admin User created successfully"));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest request) {
        try {
            String token = userService.loginUser(request);
            ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)       // Hides it from Angular/JavaScript (Prevents XSS)
                    .secure(true)// IMPORTANT: Change to true when you deploy with HTTPS!
                    .path("/")// Tells the browser to send it on every API call
                    .maxAge(24 * 60 * 60) // 1 day expiration (matches your token expiration)
                    .sameSite("None")      // Basic CSRF protection
                    .build();

            // 3. Send the cookie in the Header, NOT in the body
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(java.util.Map.of("message", "Login Successful!"));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getErrorMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // Overwrite the existing cookie with an empty one that expires instantly
        ResponseCookie cleanCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // 0 maxAge tells the browser to delete the cookie immediately
                .sameSite("None")
                .build();

        ResponseCookie sessionCookie = ResponseCookie.from("JSESSIONID", "")
                .httpOnly(true)
                .secure(true) // Change to true in production
                .path("/hoodle")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cleanCookie.toString())
                .header(HttpHeaders.SET_COOKIE, sessionCookie.toString())
                .body(java.util.Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/init")
    public ResponseEntity<?> getAppInitData(Authentication authentication) {
        try {
            // Because of your JwtAuthFilter, authentication.getName() perfectly returns the user's email!
            UUID userId = UUID.fromString(authentication.getName());

            InitResponse initData = userService.getInitData(userId);

            return ResponseEntity.ok(initData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/users/login")
    public List<User> getAllUserLogin(){
        return userService.getAllUsers();
    }



}
