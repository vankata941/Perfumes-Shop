package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.config.ForexConfig;
import com.softuni.perfumes_shop.model.dto.inbound.ExchangeRatesDTO;
import com.softuni.perfumes_shop.model.entity.ExchangeRate;
import com.softuni.perfumes_shop.repository.ExchangeRateRepository;
import com.softuni.perfumes_shop.service.ExchangeRateService;
import com.softuni.perfumes_shop.service.exception.ApiObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateServiceImpl.class);
    private final ExchangeRateRepository exchangeRateRepository;
    private final RestClient restClient;
    private final ForexConfig forexConfig;

    @Override
    public boolean hasInitializedExRates() {
        return exchangeRateRepository.count() > 0;
    }

    @Override
    public ExchangeRatesDTO fetchExchangeRates() {
        return restClient
                .get()
                .uri(forexConfig.getUrl(), forexConfig.getKey())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(ExchangeRatesDTO.class);
    }

    @Override
    public void updateExchangeRates(ExchangeRatesDTO exchangeRatesDTO) {

        LOGGER.info("Updating exchange rates for {} currencies.", exchangeRatesDTO.getRates().size());

        if (!forexConfig.getBase().equals(exchangeRatesDTO.getBase())) {
            throw new IllegalArgumentException("Incorrect base for currencies.");
        }
        exchangeRatesDTO.getRates().forEach((currency, rate) -> {
            Optional<ExchangeRate> optExRate = exchangeRateRepository
                    .findByCurrency(currency);
            if (optExRate.isPresent()) {
                optExRate.get().setRate(rate);
                exchangeRateRepository.save(optExRate.get());
            } else {
                ExchangeRate exRate = new ExchangeRate();
                exRate.setCurrency(currency);
                exRate.setRate(rate);
                exchangeRateRepository.save(exRate);
            }
        });


    }

    @Override
    public void initializeExchangeRates() {
        if (!hasInitializedExRates()) {
            updateExchangeRates(fetchExchangeRates());
        }
    }

    @Override
    public Optional<BigDecimal> findExchangeRate(String from, String to) {
        if (from.equals(to)) {
            return Optional.of(BigDecimal.ONE);
        }

        Optional<BigDecimal> optFrom = forexConfig.getBase().equals(from) ?
                Optional.of(BigDecimal.ONE)
                : exchangeRateRepository.findByCurrency(from).map(ExchangeRate::getRate);

        Optional<BigDecimal> optTo = forexConfig.getBase().equals(to) ?
                Optional.of(BigDecimal.ONE)
                : exchangeRateRepository.findByCurrency(to).map(ExchangeRate::getRate);

        if (optFrom.isEmpty() || optTo.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(optTo.get().divide(optFrom.get(), 2, RoundingMode.HALF_DOWN));
        }
    }

    @Override
    public BigDecimal convert(String from, String to, BigDecimal amount) {
        return findExchangeRate(from, to)
                .orElseThrow(() -> new ApiObjectNotFoundException("Conversion from " + from + " to " + to + " not possible!"))
                .multiply(amount);
    }
}
