package com.example.hoodle.controller;

import com.example.hoodle.Entity.UserLogin;
import com.example.hoodle.Exception.CustomException;
import com.example.hoodle.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> userRegisration(@RequestBody UserLogin userLogin) throws CustomException{
             userService.saveUser(userLogin);
             return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody UserLogin userLogin) {
            String token = userService.loginUser(userLogin);
            return ResponseEntity.ok(Map.of("token",token,"message","Login Successful !"));
    }

    @GetMapping("/users")
    public List<UserLogin> getAllUsers(){
        return userService.getAllUsers();
    }

}
