package com.softuni.perfumes_shop.model.dto.inbound;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeRatesDTO {

    private String base;

    private Map<String, BigDecimal> rates = new HashMap<>();

}
