package com.softuni.perfumes_shop.model.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    CREATED("Created"),
    CONFIRMED("Confirmed"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }
}
