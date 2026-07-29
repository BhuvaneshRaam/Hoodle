package com.example.hoodle.Service;

import com.example.hoodle.DTO.ModulePermissionBean;
import com.example.hoodle.DTO.PrivilegeBean;
import com.example.hoodle.Entity.Permission;
import com.example.hoodle.Repository.PermissionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepo permissionRepo;

    public List<ModulePermissionBean> getGroupedPermissions() {
        List<Permission> allPermissions = permissionRepo.findAll();


        Map<String, List<Permission>> groupedByModule = allPermissions.stream()
                .collect(Collectors.groupingBy(p -> p.getModule().getName()));


        return groupedByModule.entrySet().stream().map(entry -> {
            String moduleName = entry.getKey();

            List<PrivilegeBean> privileges = entry.getValue().stream()
                    .map(p -> new PrivilegeBean(p.getId(), p.getPrivilege().getName()))
                    .collect(Collectors.toList());

            return new ModulePermissionBean(moduleName, privileges);
        }).collect(Collectors.toList());
    }
}
