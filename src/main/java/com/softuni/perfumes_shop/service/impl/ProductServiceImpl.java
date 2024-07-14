package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.model.dto.AddProductDTO;
import com.softuni.perfumes_shop.model.dto.ViewProductDTO;
import com.softuni.perfumes_shop.model.entity.Image;
import com.softuni.perfumes_shop.model.entity.Product;
import com.softuni.perfumes_shop.model.entity.Type;
import com.softuni.perfumes_shop.repository.ProductRepository;
import com.softuni.perfumes_shop.service.ImageService;
import com.softuni.perfumes_shop.service.ProductService;
import com.softuni.perfumes_shop.service.TypeService;
import jakarta.persistence.NonUniqueResultException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ImageService imageService;
    private final TypeService typeService;

    @Override
    @Transactional
    public void addProduct(AddProductDTO productData) {

        if (productRepository.existsByName(productData.getName())) {
            throw new NonUniqueResultException("Product with this name already exists");
        }

        Product product = modelMapper.map(productData, Product.class);

        Optional<Type> optType = typeService.findByProductTypeName(productData.getProductTypeName());

        if (optType.isPresent()) {
            product.setType(optType.get());
        } else {
            throw new IllegalArgumentException("Invalid product type");
        }

        if (!productData.getImage().isEmpty() && productData.getImage() != null) {
            imageService.uploadImage(productData.getImage());
            Optional<Image> optImage = imageService.findByName(productData.getImage().getOriginalFilename());
            if (optImage.isPresent()) {
                product.setImage(optImage.get());
            } else {
                imageService.deleteImage(productData.getImage().getOriginalFilename());
                throw new IllegalArgumentException("Invalid image");
            }
        }

        productRepository.save(product);
    }

    @Override
    public List<ViewProductDTO> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();

        return allProducts.stream().map(p -> {
            ViewProductDTO viewProductDTO = modelMapper.map(p, ViewProductDTO.class);
            viewProductDTO.setInStock(p.isInStock());
            return viewProductDTO;
        }).toList();
    }
}
