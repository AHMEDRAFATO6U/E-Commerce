package com.boot_demo1.ecommerce_springboot.Repo.Security;

import com.boot_demo1.ecommerce_springboot.Model.Security.AppRole;
import com.boot_demo1.ecommerce_springboot.Model.Security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
