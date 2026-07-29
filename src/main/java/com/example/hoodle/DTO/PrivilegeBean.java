package com.example.hoodle.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegeBean {
    private Long permissionId;
    private String privilegeName;
}
