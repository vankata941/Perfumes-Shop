package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.model.entity.Image;
import com.softuni.perfumes_shop.repository.ImageRepository;
import com.softuni.perfumes_shop.service.ImageService;
import jakarta.persistence.NonUniqueResultException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);
    private final ImageRepository imageRepository;

    @Override
    public void uploadImage(MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Please upload an image!");
        }

        if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new IllegalArgumentException("Invalid file");
        }

        if (imageRepository.existsByName(file.getOriginalFilename())) {
            throw new NonUniqueResultException("File with this name already exists");
        }

        Image image = new Image();
        image.setName(file.getOriginalFilename());
        try {
            image.setImage(file.getBytes());
        } catch (IOException e) {
            log.info(e.getMessage());
            return;
        }
        
        imageRepository.save(image);
    }

    @Override
    public Optional<Image> findByName(String originalFileName) {
        return imageRepository.findByName(originalFileName);
    }

    @Override
    public void deleteImage(String originalFilename) {
        imageRepository.deleteByName(originalFilename);
    }
}
