package com.example.hoodle.controller;

import com.example.hoodle.DTO.RoleDto;
import com.example.hoodle.DTO.RoleRequest;
import com.example.hoodle.Exception.CustomException;
import com.example.hoodle.Service.RoleService;
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
@RequestMapping("/api/v1/role")
@PreAuthorize("hasAuthority('TENANT_ADMIN')")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllRolesForTenant(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search,
            Authentication authentication) {
        try {
            UUID adminUserId = UUID.fromString(authentication.getName());
            Page<RoleDto> roles = roleService.getRolesForTenant(adminUserId, search, page, size);

            return ResponseEntity.ok(roles);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getRolesList(Authentication authentication) {
        try {
            UUID adminUserId = UUID.fromString(authentication.getName());
            List<RoleDto> roles = roleService.getAllRolesListForTenant(adminUserId);
            return ResponseEntity.ok(roles);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
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
