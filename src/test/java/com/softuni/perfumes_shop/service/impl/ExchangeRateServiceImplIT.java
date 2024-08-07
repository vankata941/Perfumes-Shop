package com.softuni.perfumes_shop.service.impl;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import com.softuni.perfumes_shop.config.ForexConfig;
import com.softuni.perfumes_shop.init.Initializer;
import com.softuni.perfumes_shop.model.dto.inbound.ExchangeRatesDTO;
import com.softuni.perfumes_shop.model.entity.ExchangeRate;
import com.softuni.perfumes_shop.repository.ExchangeRateRepository;
import com.softuni.perfumes_shop.service.ExchangeRateService;
import com.softuni.perfumes_shop.service.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

@SpringBootTest
@EnableWireMock(
    @ConfigureWireMock(name = "exchange-rate-service")
)
public class ExchangeRateServiceImplIT {

    @InjectWireMock("exchange-rate-service")
    private WireMockServer wireMockServer;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private ForexConfig forexConfig;

    @MockBean
    private Initializer initializer;

    @MockBean
    private JwtService jwtService;


    @BeforeEach
    public void setUp() {
        forexConfig.setUrl(wireMockServer.baseUrl() + "/test-exchange-rates");
        mockServer();
    }

    @AfterEach
    public void tearDown() {
        exchangeRateRepository.deleteAll();
    }

    @Test
    public void testFetchExchangeRates() {
        
        ExchangeRatesDTO exchangeRatesDTO = exchangeRateService.fetchExchangeRates();

        Assertions.assertEquals("USD", exchangeRatesDTO.getBase());
        Assertions.assertEquals(2, exchangeRatesDTO.getRates().size());
        Assertions.assertEquals(new BigDecimal("1.86"), exchangeRatesDTO.getRates().get("BGN"));
        Assertions.assertEquals(new BigDecimal("0.91"), exchangeRatesDTO.getRates().get("EUR"));
    }

    @Test
    public void testUpdateExchangeRates() {

        ExchangeRate exRate = new ExchangeRate();
        exRate.setCurrency("BGN");
        exRate.setRate(new BigDecimal("1.80"));

        exchangeRateRepository.save(exRate);

        ExchangeRatesDTO exchangeRatesDTO = exchangeRateService.fetchExchangeRates();
        exchangeRateService.updateExchangeRates(exchangeRatesDTO);

        Assertions.assertEquals(2L, exchangeRateRepository.count());

        Optional<ExchangeRate> optRateBGN = exchangeRateRepository.findByCurrency("BGN");

        Assertions.assertTrue(optRateBGN.isPresent());

        ExchangeRate exchangeRateBGN = optRateBGN.get();


        Assertions.assertEquals(new BigDecimal("1.86"), exchangeRateBGN.getRate());

        Optional<ExchangeRate> optRateEUR = exchangeRateRepository.findByCurrency("EUR");

        Assertions.assertTrue(optRateEUR.isPresent());

        ExchangeRate exchangeRateEUR = optRateEUR.get();


        Assertions.assertEquals(new BigDecimal("0.91"), exchangeRateEUR.getRate());

    }

    @Test
    public void testUpdateThrowsIllegalArgumentExc() {
        ExchangeRatesDTO exchangeRatesDTO = exchangeRateService.fetchExchangeRates();
        exchangeRatesDTO.setBase("BGN");

        Assertions.assertThrows(IllegalArgumentException.class, () -> exchangeRateService.updateExchangeRates(exchangeRatesDTO));
    }


    private void mockServer() {
        wireMockServer.stubFor(get("/test-exchange-rates").willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(
                        """
                            {
                                "base": "USD",
                                "rates": {
                                        "BGN": 1.86,
                                        "EUR": 0.91
                                }
                            }
                        """
                )
        ));
    }

}