package com.example.hoodle.Repository;

import com.example.hoodle.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepo extends JpaRepository<Permission, Long> {
}
