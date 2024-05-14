package com.onlinebookstore.onlinebookstore.repository;

import com.onlinebookstore.onlinebookstore.model.roles.Role;
import com.onlinebookstore.onlinebookstore.model.roles.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRolesName(RoleName roleName);
}
