package com.example.hoodle.Repository;

import com.example.hoodle.Entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserLogin, Long> {

    boolean existsByUserName(String userName);
    Optional<UserLogin> findByUserName(String userName);

}
