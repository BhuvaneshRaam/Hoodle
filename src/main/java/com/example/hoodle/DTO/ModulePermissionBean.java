package com.example.hoodle.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModulePermissionBean {
    private String moduleName;
    private List<PrivilegeBean> availablePrivileges;
}
