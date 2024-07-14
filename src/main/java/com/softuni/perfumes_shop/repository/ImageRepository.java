package com.softuni.perfumes_shop.repository;

import com.softuni.perfumes_shop.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    boolean existsByName(String originalFilename);

    Optional<Image> findByName(String originalFileName);

    void deleteByName(String originalFilename);

}
