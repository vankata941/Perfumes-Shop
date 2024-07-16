package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.entity.Role;
import com.softuni.perfumes_shop.model.enums.UserRole;

import java.util.Optional;

public interface RoleService {

    void initializeRoles();

    Optional<Role> findByUserRole(UserRole userRole);

    Optional<Role> findByUserRoleName(String userRoleName);
}
