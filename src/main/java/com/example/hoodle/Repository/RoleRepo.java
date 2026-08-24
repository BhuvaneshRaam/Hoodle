package com.example.hoodle.Repository;

import com.example.hoodle.Entity.Role;
import com.example.hoodle.Entity.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    Optional<Role> findByNameAndIsSystemRoleTrue(String name);

    boolean existsByNameAndTenant(String name, Tenant tenant);

    List<Role> findByTenantOrTenantIsNull(Tenant tenant);

    List<Role> findByTenantOrIsSystemRoleTrue(Tenant tenant);

    @Query("SELECT r FROM Role r WHERE (r.tenant = :tenant OR r.isSystemRole = true) AND " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Role> searchRolesByTenant(
            @Param("tenant") Tenant tenant,
            @Param("search") String search,
            Pageable pageable
    );
}
