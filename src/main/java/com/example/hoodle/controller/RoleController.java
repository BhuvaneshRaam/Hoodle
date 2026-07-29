package com.example.hoodle.controller;

import com.example.hoodle.DTO.RoleRequest;
import com.example.hoodle.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
@PreAuthorize("hasAuthority('TENANT_ADMIN')")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<?> getAllRolesForTenant(Authentication authentication) {
        UUID adminUserId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(roleService.getRolesForTenant(adminUserId));
    }

    @PostMapping
    public ResponseEntity<?> createCustomRole(@RequestBody RoleRequest request, Authentication authentication) {
        try {
            UUID adminUserId = UUID.fromString(authentication.getName());
            roleService.createCustomRole(request, adminUserId);
            return ResponseEntity.ok(Map.of("message", "Custom role created successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{roleId}")
    public ResponseEntity<?> updateCustomRole(@PathVariable Long roleId, @RequestBody RoleRequest request, Authentication authentication) {
        try {
            UUID adminUserId = UUID.fromString(authentication.getName());
            roleService.updateCustomRole(roleId, request, adminUserId);
            return ResponseEntity.ok(Map.of("message", "Custom role updated successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
