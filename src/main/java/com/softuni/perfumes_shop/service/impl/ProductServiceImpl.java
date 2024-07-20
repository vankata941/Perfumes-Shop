package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.model.dto.inbound.AddProductDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ViewProductDTO;
import com.softuni.perfumes_shop.model.entity.Image;
import com.softuni.perfumes_shop.model.entity.Product;
import com.softuni.perfumes_shop.model.entity.Type;
import com.softuni.perfumes_shop.repository.ProductRepository;
import com.softuni.perfumes_shop.service.ImageService;
import com.softuni.perfumes_shop.service.ProductService;
import com.softuni.perfumes_shop.service.TypeService;
import com.softuni.perfumes_shop.service.exception.ObjectNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
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

        return allProducts.stream().map(this::mapProduct).toList();
    }

    private ViewProductDTO mapProduct(Product product) {
        ViewProductDTO viewProductDTO = modelMapper.map(product, ViewProductDTO.class);
        viewProductDTO.setInStock(product.isInStock());
        if (product.getImage() != null) {
            viewProductDTO.setImage(Base64.getEncoder().encodeToString(product.getImage().getImage()));
        }
        if (product.getType() != null) {
            viewProductDTO.setProductTypeName(product.getType().getProductType().getName());
        }
        return viewProductDTO;
    }

    @Override
    public ViewProductDTO getProductById(Long id) {
        Optional<Product> optProduct = productRepository.findById(id);
        if (optProduct.isEmpty()) {
            throw new ObjectNotFoundException("Product with id " + id + " was not found!", id);
        }

        return mapProduct(optProduct.get());
    }

    @Override
    public void deleteProductById(Long id) {

        productRepository.deleteById(id);
    }

    @Override
    public String findNameOfProduct(Long id) {
        Optional<Product> optProduct = productRepository.findById(id);
        return optProduct.map(Product::getName).orElse(null);
    }
}
