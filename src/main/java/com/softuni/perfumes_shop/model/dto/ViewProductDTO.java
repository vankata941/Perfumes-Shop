package com.softuni.perfumes_shop.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViewProductDTO {

    private String name;
    private String description;
    private Double price;
    private boolean isInStock;
}
