package com.softuni.perfumes_shop.model.dto.outbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversionResultDTO {

    private String from;
    private String to;
    private BigDecimal amount;
    private BigDecimal result;

}
