package com.example.hoodle.Repository;

import com.example.hoodle.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {

    boolean existsByUserName(String userName);
    boolean existsByEmailId(String  emailId);
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmailId(String emaildId);

}
