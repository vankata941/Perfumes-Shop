package com.softuni.perfumes_shop.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "exchange_rates")
public class ExchangeRate extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String currency;

    @Column(nullable = false)
    @Positive
    private BigDecimal rate;
}
