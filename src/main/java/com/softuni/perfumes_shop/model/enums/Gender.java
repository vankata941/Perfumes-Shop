package com.softuni.perfumes_shop.model.enums;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    UNISEX("Unisex");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }
}
