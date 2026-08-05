package com.example.hoodle.Repository;

import com.example.hoodle.Entity.Tenant;
import com.example.hoodle.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {

    boolean existsByUserName(String userName);
    boolean existsByEmailId(String  emailId);
    Optional<User> findByUserName(String userName);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByEmailId(String emaildId);

    List<User> findByTenant(Tenant tenant);

    @Query("SELECT u FROM User u WHERE u.tenant = :tenant AND " +
            "(LOWER(u.userName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.emailId) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<User> searchUsersByTenant(
            @Param("tenant") Tenant tenant,
            @Param("search") String search,
            Pageable pageable
    );

}
