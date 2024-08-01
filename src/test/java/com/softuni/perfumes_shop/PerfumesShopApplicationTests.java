package com.softuni.perfumes_shop;

import com.softuni.perfumes_shop.init.Initializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class PerfumesShopApplicationTests {

    @MockBean
    private Initializer initializer;

    @Test
    void contextLoads() {
    }

}
