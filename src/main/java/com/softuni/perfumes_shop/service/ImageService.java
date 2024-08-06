package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.dto.outbound.CarouselDTO;
import com.softuni.perfumes_shop.model.entity.Image;
import com.softuni.perfumes_shop.model.enums.ImageType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ImageService {

    void uploadImage(MultipartFile file, ImageType imageType);

    Optional<Image> findByName(String originalFileName);

    void deleteImage(String originalFilename);

    List<CarouselDTO> getCarousels();
}
