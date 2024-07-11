package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.model.entity.Role;
import com.softuni.perfumes_shop.model.enums.UserRole;
import com.softuni.perfumes_shop.repository.RoleRepository;
import com.softuni.perfumes_shop.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public void initializeRoles() {
        if (this.roleRepository.count() == 0) {
            UserRole[] userRoles = UserRole.values();
            for (UserRole userRole : userRoles) {
                Role role = new Role();
                role.setUserRole(userRole);

                roleRepository.save(role);
            }
        }
    }
}