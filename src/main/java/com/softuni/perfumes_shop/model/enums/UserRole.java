package com.softuni.perfumes_shop.model.enums;

import lombok.Getter;

@Getter
public enum UserRole {
        USER("User"),
        ADMIN("Admin");

        private final String name;

        UserRole(String name) {
                this.name = name;
        }
}
