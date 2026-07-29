package com.example.hoodle.DTO;

import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String userName;
    private String emailId;
    private Set<String> roleNames;
    private Boolean isActive;
}
