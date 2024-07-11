package com.softuni.perfumes_shop.init;

import com.softuni.perfumes_shop.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final RoleService roleService;

    @Override
    public void run(String... args) throws Exception {

        roleService.initializeRoles();
    }
}
