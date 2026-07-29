package com.example.hoodle.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {
    private String roleName;
    private Set<Long> permissionIds;
    private Boolean isActive;
}
