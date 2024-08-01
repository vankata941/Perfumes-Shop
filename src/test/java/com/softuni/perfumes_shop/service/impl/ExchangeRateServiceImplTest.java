package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.config.ForexConfig;
import com.softuni.perfumes_shop.model.entity.ExchangeRate;
import com.softuni.perfumes_shop.repository.ExchangeRateRepository;
import com.softuni.perfumes_shop.service.exception.ApiObjectNotFoundException;
import com.softuni.perfumes_shop.service.impl.ExchangeRateServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mock.Strictness;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceImplTest {

    private static final class TestRates {

        private static final String BASE = "AAA";

        private static final ExchangeRate BBB = new ExchangeRate();
        static {
        BBB.setCurrency("BBB");
        BBB.setRate(new BigDecimal("2"));
        }

        private static final ExchangeRate CCC = new ExchangeRate();
        static {
            CCC.setCurrency("CCC");
            CCC.setRate(new BigDecimal("0.5"));
        }

    }

    private ExchangeRateServiceImpl toTest;

    @Mock(strictness = Strictness.LENIENT)
    private ExchangeRateRepository mockRepository;

    @BeforeEach
    public void setUp() {
        ForexConfig config = new ForexConfig();
        config.setBase(TestRates.BASE);
        toTest = new ExchangeRateServiceImpl(
            mockRepository,
            null,
            config
        );
    }

    @Test
    public void testHasInitializedExRates() {
        when(mockRepository.count()).thenReturn(0L);
        Assertions.assertFalse(toTest.hasInitializedExRates());

        when(mockRepository.count()).thenReturn(-1L);
        Assertions.assertFalse(toTest.hasInitializedExRates());

        when(mockRepository.count()).thenReturn( 1L);
        Assertions.assertTrue(toTest.hasInitializedExRates());
    }

    @Test
    public void testConvertThrows() {
        Assertions.assertThrows(ApiObjectNotFoundException.class, () -> toTest.convert("Not_Existing_1", "Not_Existing_2", BigDecimal.ONE));
    }

    @ParameterizedTest
    @CsvSource(
        textBlock = """
        AAA, BBB, 1, 2.00
        AAA, CCC, 2, 1.00
        BBB, CCC, 2, 0.50
        CCC, BBB, 1, 4.00
        AAA, AAA, 1, 1
        BBB, BBB, 1, 1
        CCC, CCC, 1, 1
        """
    )
    public void testConvert(String from, String to, BigDecimal amount, BigDecimal expected) {
        
        when(mockRepository.findByCurrency(TestRates.BBB.getCurrency())).thenReturn(Optional.of(TestRates.BBB));
        when(mockRepository.findByCurrency(TestRates.CCC.getCurrency())).thenReturn(Optional.of(TestRates.CCC));

        BigDecimal result = toTest.convert(from, to, amount);

        Assertions.assertEquals(expected, result);
    }
}