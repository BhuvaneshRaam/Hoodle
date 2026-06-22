package com.example.hoodle.Repository;

import com.example.hoodle.Entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepo extends JpaRepository<Tenant, Long> {
    boolean existsByName(String name);
}
