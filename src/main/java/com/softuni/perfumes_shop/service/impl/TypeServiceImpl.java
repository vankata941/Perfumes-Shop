package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.model.entity.Type;
import com.softuni.perfumes_shop.model.enums.PerfumeType;
import com.softuni.perfumes_shop.repository.TypeRepository;
import com.softuni.perfumes_shop.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {

    private static final String EUA_DE_TOILETTE = "Eau de Toilette: A light, refreshing fragrance with a lower concentration of perfume oils, perfect for everyday use. Subtle yet captivating aroma.";
    private static final String EAU_DE_PERFUME = "Eau de Perfume: A long-lasting, rich fragrance with a higher concentration of perfume oils. Ideal for special occasions and making a lasting impression.";
    private static final String PERFUME = "Perfume: The most concentrated and long-lasting fragrance, offering an intense and luxurious aroma that lasts all day. Perfect for special occasions.";

    private final TypeRepository typeRepository;

    @Override
    public void initializeTypes() {
        if (typeRepository.count() == 0) {
            Arrays.stream(PerfumeType.values())
                    .forEach(t -> {
                        Type type = new Type();
                        type.setPerfumeType(t);
                        type.setDescription(getDescription(t.name()));

                        typeRepository.save(type);
                    }
            );
        }
    }

    public static String getDescription(String name) {
        return switch(name.toUpperCase()) {
            case "EUA_DE_TOILETTE" -> EUA_DE_TOILETTE;
            case "EAU_DE_PERFUME" -> EAU_DE_PERFUME;
            case "PERFUME" -> PERFUME;
            default -> "No such type found!";
        };
    }
}
