package com.example.hoodle.Service;

import com.example.hoodle.Constants.ErrorCode;
import com.example.hoodle.DTO.InitResponse;
import com.example.hoodle.DTO.LoginRequest;
import com.example.hoodle.DTO.TenantRegistrationRequest;
import com.example.hoodle.Entity.Permission;
import com.example.hoodle.Entity.Role;
import com.example.hoodle.Entity.Tenant;
import com.example.hoodle.Entity.User;
import com.example.hoodle.Exception.CustomException;
import com.example.hoodle.Repository.RoleRepo;
import com.example.hoodle.Repository.TenantRepo;
import com.example.hoodle.Repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private TenantRepo tenantRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Transactional
    public void registerTenantAndAdmin(TenantRegistrationRequest request) {
            if(userRepo.existsByEmailId(request.getEmailId())) {
                throw new CustomException(ErrorCode.User_Already_Exist, "User Already Existing");
            }
            if(tenantRepo.existsByName(request.getTenantName())) {
                System.out.println("TENANT EXISTS");
                throw new CustomException("400", "Tenant Already Exists!");
            }

            Tenant newTenant = new Tenant();
            newTenant.setName(request.getTenantName());
            tenantRepo.save(newTenant);

            Role tenantAdmin = roleRepo.findByName("TENANT_ADMIN")
                    .orElseThrow(() -> new CustomException("500", "Critical Error: TENANT_ADMIN role not found in database"));

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(tenantAdmin);

            User adminUser = new User();
            adminUser.setEmailId(request.getEmailId());
            adminUser.setUserName(request.getUserName());
            adminUser.setPassword(passwordEncoder.encode(request.getPassword()));
            adminUser.setTenant(newTenant);
            adminUser.setRoles(userRoles);
            userRepo.save(adminUser);
    }

    public String loginUser(LoginRequest request) {
            if(request.getEmailId() == null || request.getPassword() == null) {
                throw new CustomException(ErrorCode.Internal_Server_Error, "User credentials not valid");
            }
            User user = userRepo.findByEmailId(request.getEmailId())
                    .orElseThrow(() -> new CustomException(ErrorCode.User_Not_found,"Invalid User !"));
            if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new CustomException(ErrorCode.Invalid_Credentials,"Invalid password!");
            }

            java.util.List<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName())
                .toList();
            return jwtGenerator.generateJwtToken(user, roleNames);
    }

    public InitResponse getInitData(UUID userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new CustomException("404","User not found!" ));

        Map<String, Set<String>> accessMap = new HashMap<>();
        List<String> roleNames = new java.util.ArrayList<>();
        if(user.getRoles() != null) {
            for (Role role: user.getRoles()) {
                if(role.getPermissions() != null) {
                    roleNames.add(role.getName());
                    for(Permission perm: role.getPermissions()) {
                        String moduleName = perm.getModule().getName();
                        String privilegeName = perm.getPrivilege().getName();
                        accessMap.computeIfAbsent(moduleName, k -> new HashSet<>()).add(privilegeName);
                    }
                }
            }
        }

        return new InitResponse(user.getEmailId(), user.getUserName(), user.getTenant().getName(),roleNames, accessMap);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
          users = userRepo.findAll();
        }
        catch (Exception e) {
            throw new CustomException("9006","Unexcpected eror occured while fetching users");
        }
        return users;
    }
}
