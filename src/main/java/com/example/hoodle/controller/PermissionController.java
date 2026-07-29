package com.example.hoodle.controller;

import com.example.hoodle.Service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/permissions")
@PreAuthorize("hasAuthority('TENANT_ADMIN')")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public ResponseEntity<?> getPermissionMatrix() {
        return ResponseEntity.ok(permissionService.getGroupedPermissions());
    }
}
