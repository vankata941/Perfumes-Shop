package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.controller.aop.WarnExecutionExceeds;
import com.softuni.perfumes_shop.model.dto.outbound.ConversionResultDTO;
import com.softuni.perfumes_shop.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class CurrencyController {

    private final ExchangeRateService exchangeRateService;

    @WarnExecutionExceeds(threshold = 1000)
    @GetMapping("/currency/convert")
    public ResponseEntity<ConversionResultDTO> convertCurrency(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("amount") BigDecimal amount
    ) {
        System.out.println("Received request to convert currency");
        BigDecimal result = exchangeRateService.convert(from, to, amount);

        return ResponseEntity.ok(new ConversionResultDTO(from, to, amount, result));
    }
}
