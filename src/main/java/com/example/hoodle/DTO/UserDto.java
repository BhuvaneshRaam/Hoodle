package com.example.hoodle.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID userUuid;
    private String userName;
    private String emailId;
    private Boolean isActive;
    private Set<RoleDto> roles;
}
