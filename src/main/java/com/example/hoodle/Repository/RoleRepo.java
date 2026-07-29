package com.example.hoodle.Repository;

import com.example.hoodle.Entity.Role;
import com.example.hoodle.Entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    boolean existsByNameAndTenant(String name, Tenant tenant);

    List<Role> findByTenantOrTenantIsNull(Tenant tenant);
}
