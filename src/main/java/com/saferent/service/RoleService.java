package com.saferent.service;

import com.saferent.entity.Role;
import com.saferent.entity.enums.RoleType;
import com.saferent.exception.ResourceNotFoundException;
import com.saferent.exception.message.ErrorMessage;
import com.saferent.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;


    public Role findByType(RoleType roleType) {

        Role role = roleRepository
                .findByRoleType(roleType)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.ROLE_NOT_FOUND_EXCEPTION, roleType.name())));

                return role;

    }
}
