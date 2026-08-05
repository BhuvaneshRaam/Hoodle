package com.example.hoodle.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private Long id;
    private String roleName;
    private Set<PermissionDto> permissions;
    private Boolean isActive;
}
