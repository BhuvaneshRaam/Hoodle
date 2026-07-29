package com.example.hoodle.Service;

import com.example.hoodle.DTO.RoleRequest;
import com.example.hoodle.Entity.Permission;
import com.example.hoodle.Entity.Role;
import com.example.hoodle.Entity.User;
import com.example.hoodle.Exception.CustomException;
import com.example.hoodle.Repository.PermissionRepo;
import com.example.hoodle.Repository.RoleRepo;
import com.example.hoodle.Repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PermissionRepo permissionRepo;

    public List<Role> getRolesForTenant(UUID adminUserId) {
        User adminUser = userRepo.findById(adminUserId)
                .orElseThrow(() -> new CustomException("401", "Admin not found"));

        return roleRepo.findByTenantOrTenantIsNull(adminUser.getTenant());
    }

    @Transactional
    public void createCustomRole(RoleRequest request, UUID adminUserId) {
        User adminUser = userRepo.findById(adminUserId)
                .orElseThrow(() -> new CustomException("401", "Admin not found"));

        if (roleRepo.existsByNameAndTenant(request.getRoleName(), adminUser.getTenant())) {
            throw new CustomException("400", "A role with this name already exists in your workspace.");
        }

        List<Permission> selectedPermissions = permissionRepo.findAllById(request.getPermissionIds());
        if (selectedPermissions.isEmpty()) {
            throw new CustomException("400", "You must select at least one permission.");
        }

        Role newRole = new Role();
        newRole.setName(request.getRoleName());
        newRole.setTenant(adminUser.getTenant()); // Lock to workspace
        newRole.setPermissions(new HashSet<>(selectedPermissions));
        newRole.setActive(true);

        roleRepo.save(newRole);
    }

    @Transactional
    public void updateCustomRole(Long roleId, RoleRequest request, UUID adminUserId) {
        User adminUser = userRepo.findById(adminUserId)
                .orElseThrow(() -> new CustomException("401", "Admin not found"));

        Role existingRole = roleRepo.findById(roleId)
                .orElseThrow(() -> new CustomException("404", "Role not found"));

        if (existingRole.getTenant() == null ||
                !existingRole.getTenant().getTenantUuid().equals(adminUser.getTenant().getTenantUuid())) {
            throw new CustomException("403", "You cannot edit system roles or roles belonging to another workspace.");
        }

        if (request.getRoleName() != null) {
            existingRole.setName(request.getRoleName());
        }

        if (request.getIsActive() != null) {
            existingRole.setActive(request.getIsActive());
        }

        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            List<Permission> selectedPermissions = permissionRepo.findAllById(request.getPermissionIds());
            existingRole.setPermissions(new HashSet<>(selectedPermissions));
        }

        roleRepo.save(existingRole);
    }

}
