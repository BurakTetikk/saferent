package com.saferent.repository;

import com.saferent.entity.Role;
import com.saferent.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {


    Optional<Role> findByRoleType(RoleType type);
}
