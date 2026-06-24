package com.example.hoodle.controller;

import com.example.hoodle.DTO.InitResponse;
import com.example.hoodle.DTO.LoginRequest;
import com.example.hoodle.DTO.TenantRegistrationRequest;
import com.example.hoodle.Entity.User;
import com.example.hoodle.Exception.CustomException;
import com.example.hoodle.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
        try {
            userService.registerTenantAndAdmin(request);
            return ResponseEntity.ok(Map.of("message", "Tenant and Admin User created successfully"));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest request) {
        try {
            String token = userService.loginUser(request);
            return ResponseEntity.ok(java.util.Map.of("token", token, "message", "Login Successful!"));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
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
