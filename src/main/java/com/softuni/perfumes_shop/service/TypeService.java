package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.entity.Type;

import java.util.Optional;

public interface TypeService {

    void initializeTypes();

    Optional<Type> findByProductTypeName(String productTypeName);
}
