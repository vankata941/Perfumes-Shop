package com.softuni.perfumes_shop;

import com.softuni.perfumes_shop.init.Initializer;
import com.softuni.perfumes_shop.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class PerfumesShopApplicationTests {

    @MockBean
    private Initializer initializer;

    @MockBean
    private JwtService jwtService;

    @Test
    void contextLoads() {
    }

}
