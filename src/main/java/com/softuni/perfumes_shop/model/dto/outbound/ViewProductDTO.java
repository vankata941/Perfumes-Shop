package com.softuni.perfumes_shop.model.dto.outbound;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ViewProductDTO {

    private Long id;
    private String brand;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean isInStock;
    private String packaging;
    private String image;
    private String productTypeName;
}
