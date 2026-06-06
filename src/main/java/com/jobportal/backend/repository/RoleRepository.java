package com.jobportal.backend.repository;

import com.jobportal.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleById(Long id);

    Optional<Role> findRoleByName(String name);
}
