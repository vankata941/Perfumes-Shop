package com.softuni.perfumes_shop.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import com.softuni.perfumes_shop.init.Initializer;
import com.softuni.perfumes_shop.service.ExchangeRateService;
import com.softuni.perfumes_shop.service.JwtService;
import com.softuni.perfumes_shop.service.exception.ApiObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService mockExchangeRateService;

    @MockBean
    private Initializer initializer;

    @MockBean
    private JwtService jwtService;

    @Test
    public void testConvert() throws Exception {
        String from = "AAA";
        String to = "BBB";
        BigDecimal amount = new BigDecimal("2");
        BigDecimal expectedResult = new BigDecimal("3");

        when(mockExchangeRateService.convert(from, to, amount)).thenReturn(expectedResult);

        mockMvc.perform(get("/currency/convert")
        .param("from", from)
        .param("to", to)
        .param("amount", String.valueOf(amount.intValue()))
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.from").value(from))
        .andExpect(jsonPath("$.to").value(to))
        .andExpect(jsonPath("$.amount").value(amount))
        .andExpect(jsonPath("$.result").value(expectedResult));
    }

    @Test
    public void testConversionNotFound() throws Exception {
        String from = "AAA";
        String to = "BBB";
        BigDecimal amount = new BigDecimal("5");

        when(mockExchangeRateService.convert(from, to, amount))
        .thenThrow(new ApiObjectNotFoundException("Test conversion throws exception."));

        mockMvc.perform(get("/currency/convert")
        .param("from", from)
        .param("to", to)
        .param("amount", String.valueOf(amount.intValue()))
        ).andExpect(status().isNotFound());
    }
}