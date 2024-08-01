package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ImageService {

    void uploadImage(MultipartFile file);

    Optional<Image> findByName(String originalFileName);

    void deleteImage(String originalFilename);
}
