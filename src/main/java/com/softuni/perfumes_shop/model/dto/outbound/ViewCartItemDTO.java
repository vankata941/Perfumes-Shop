package com.softuni.perfumes_shop.model.dto.outbound;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ViewCartItemDTO {

    private Long id;

    private String brand;

    private String name;

    private BigDecimal price;

    private int quantity;

    private String packaging;

    private String image;
}
