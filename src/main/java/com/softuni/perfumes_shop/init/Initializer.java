package com.softuni.perfumes_shop.init;

import com.softuni.perfumes_shop.service.ExchangeRateService;
import com.softuni.perfumes_shop.service.RoleService;
import com.softuni.perfumes_shop.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final RoleService roleService;
    private final TypeService typeService;
    private final ExchangeRateService exchangeRateService;

    @Override
    public void run(String... args) {

        roleService.initializeRoles();
        typeService.initializeTypes();
        exchangeRateService.initializeExchangeRates();
    }
}
