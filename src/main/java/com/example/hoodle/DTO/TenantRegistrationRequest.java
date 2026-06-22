package com.example.hoodle.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantRegistrationRequest {
    private String tenantName;
    private String emailId;
    private String userName;
    private String password;
}
