package com.softuni.perfumes_shop.model.enums;

import lombok.Getter;

@Getter
public enum ProductType {
    EAU_DE_TOILETTE("Eau de Toilette"),
    EAU_DE_PERFUME("Eau de Perfume"),
    PERFUME("Perfume");

    private final String name;

    ProductType(String name) {
        this.name = name;
    }

}
