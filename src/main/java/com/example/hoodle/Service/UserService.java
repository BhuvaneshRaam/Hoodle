package com.example.hoodle.Service;

import com.example.hoodle.Constants.ErrorCode;
import com.example.hoodle.Entity.UserLogin;
import com.example.hoodle.Exception.CustomException;
import com.example.hoodle.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtGenerator jwtGenerator;

    public void saveUser(UserLogin userLogin) {
        try {
            if(userRepo.existsByUserName(userLogin.getUserName())){
                throw new CustomException(ErrorCode.User_Already_Exist, "User Already Existing");
            }
            userLogin.setPassword(passwordEncoder.encode(userLogin.getPassword()));
            userRepo.save(userLogin);
        }
        catch(Exception e) {
            throw new CustomException(ErrorCode.User_Registration_Error, "Unexpected error occurred while registering new user !");
        }

    }

    public String loginUser(UserLogin userLogin) {
            if(userLogin.getUserName() == null || userLogin.getPassword() == null) {
                throw new CustomException(ErrorCode.Internal_Server_Error, "User credentials not valid");
            }
            UserLogin user = userRepo.findByUserName(userLogin.getUserName())
                    .orElseThrow(() -> new CustomException(ErrorCode.User_Not_found,"Invalid Username!"));
            if(!passwordEncoder.matches(userLogin.getPassword(), user.getPassword())) {
                throw new CustomException(ErrorCode.Invalid_Credentials,"Invalid password!");
            }
            return jwtGenerator.generateJwtToken(user,"User");
    }

    public List<UserLogin> getAllUsers() {
        List<UserLogin> users = new ArrayList<>();
        try {
          users = userRepo.findAll();
        }
        catch (Exception e) {
            throw new CustomException("9006","Unexcpected eror occured while fetching users");
        }
        return users;
    }
}
