package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.dto.inbound.ExchangeRatesDTO;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeRateService {

    boolean hasInitializedExRates();

    ExchangeRatesDTO fetchExchangeRates();

    void updateExchangeRates(ExchangeRatesDTO exchangeRatesDTO);

    void initializeExchangeRates();

    Optional<BigDecimal> findExchangeRate(String from, String to);

    BigDecimal convert(String from, String to, BigDecimal amount);
}
