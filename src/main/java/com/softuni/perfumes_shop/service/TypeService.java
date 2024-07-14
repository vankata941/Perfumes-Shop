package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.entity.Type;
import com.softuni.perfumes_shop.model.enums.PerfumeType;

import java.util.Optional;

public interface TypeService {

    void initializeTypes();

    Optional<Type> findByPerfumeType(PerfumeType perfumeType);
}
