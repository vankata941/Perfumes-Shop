package com.softuni.perfumes_shop.repository;

import com.softuni.perfumes_shop.model.entity.Product;
import com.softuni.perfumes_shop.model.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    Optional<Product> findByName(String name);

    List<Product> findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(String name, String brand);

    List<Product> findAllByGender(Gender gender);
}
