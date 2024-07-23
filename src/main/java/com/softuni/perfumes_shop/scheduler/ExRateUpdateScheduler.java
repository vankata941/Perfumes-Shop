package com.softuni.perfumes_shop.scheduler;

import com.softuni.perfumes_shop.model.dto.inbound.ExchangeRatesDTO;
import com.softuni.perfumes_shop.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExRateUpdateScheduler {

    private final Logger LOGGER = LoggerFactory.getLogger(ExRateUpdateScheduler.class);
    private final ExchangeRateService exchangeRateService;

    @Scheduled(cron = "0 30 2 * * ?", zone = "Europe/Sofia")
    private void updateExRatesScheduler() {

        LOGGER.info("Starting Ex Rate Update Scheduler...");

        ExchangeRatesDTO exchangeRatesDTO = exchangeRateService.fetchExchangeRates();
        exchangeRateService.updateExchangeRates(exchangeRatesDTO);

        LOGGER.info("Finished updating exchange rates for {} currencies.", exchangeRatesDTO.getRates().size());
    }
}
