package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.dto.inbound.ExchangeRatesDTO;

import java.math.BigDecimal;

public interface ExchangeRateService {

    boolean hasInitializedExRates();

    ExchangeRatesDTO fetchExchangeRates();

    void updateExchangeRates(ExchangeRatesDTO exchangeRatesDTO);

    void initializeExchangeRates();

    BigDecimal convert(String from, String to, BigDecimal amount);
}
