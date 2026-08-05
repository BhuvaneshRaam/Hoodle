package com.example.hoodle.controller;

import com.example.hoodle.DTO.UserDto;
import com.example.hoodle.DTO.UserRequest;
import com.example.hoodle.Entity.User;
import com.example.hoodle.Exception.CustomException;
import com.example.hoodle.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user")
@PreAuthorize("hasAuthority('TENANT_ADMIN')")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsersForTenant(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search,
            Authentication authentication) {
        try {
            UUID adminUserId = UUID.fromString(authentication.getName());
            Page<UserDto> users = userService.getAllUsersForTenant(adminUserId, search, page, size);
            return ResponseEntity.ok(users);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createTenantUser(@RequestBody UserRequest request, Authentication authentication) {
        try {
            UUID adminUserId = UUID.fromString(authentication.getName());
            userService.createTenantUser(request, adminUserId);

            return ResponseEntity.ok(Map.of(
                    "message", "User created successfully!"
            ));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> updateTenantUser(@PathVariable UUID userId, @RequestBody UserRequest request, Authentication authentication) {
        try {
            UUID adminUserId = UUID.fromString(authentication.getName());
            userService.updateTenantUser(userId, request, adminUserId);
            return ResponseEntity.ok(Map.of("message", "User updated successfully."));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
