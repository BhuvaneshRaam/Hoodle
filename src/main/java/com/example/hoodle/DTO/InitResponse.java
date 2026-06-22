package com.example.hoodle.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InitResponse {
    private String emailId;
    private String userName;
    private String tenantName;
    private List<String> roles;
    private Map<String, Set<String>> access;

}
