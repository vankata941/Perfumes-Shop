package com.softuni.perfumes_shop.service.impl;


import com.softuni.perfumes_shop.model.dto.outbound.ViewProductDTO;
import com.softuni.perfumes_shop.model.entity.Image;
import com.softuni.perfumes_shop.model.entity.Product;
import com.softuni.perfumes_shop.model.entity.Type;
import com.softuni.perfumes_shop.model.enums.Gender;
import com.softuni.perfumes_shop.model.enums.ProductType;
import com.softuni.perfumes_shop.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    private ProductServiceImpl toTest;

    @Mock
    private ProductRepository mockRepository;

    @BeforeEach
    public void setUp() {
        toTest = new ProductServiceImpl(
            mockRepository,
            new ModelMapper(),
            null,
            null
        );
    }

    @Test
    public void testGetMaleProducts() {
        when(mockRepository.findAllByGender(Gender.MALE)).thenReturn(new ArrayList<>());

        List<ViewProductDTO> maleProducts;

        maleProducts = toTest.getMaleProducts();

        Assertions.assertEquals(0, maleProducts.size());

        Product product = createProduct();
        product.setGender(Gender.MALE);
        when(mockRepository.findAllByGender(Gender.MALE)).thenReturn(List.of(product));

        maleProducts = toTest.getMaleProducts();

        Assertions.assertEquals(1, maleProducts.size());

    }

    @Test
    public void testGetFemaleProducts() {
        when(mockRepository.findAllByGender(Gender.FEMALE)).thenReturn(new ArrayList<>());

        List<ViewProductDTO> femaleProducts;
        femaleProducts = toTest.getFemaleProducts();

        Assertions.assertEquals(0, femaleProducts.size());

        Product product = createProduct();
        product.setGender(Gender.FEMALE);
        when(mockRepository.findAllByGender(Gender.FEMALE)).thenReturn(List.of(product));

        femaleProducts = toTest.getFemaleProducts();

        Assertions.assertEquals(1, femaleProducts.size());

    }

    @Test
    public void testGetUnisexProducts() {
        when(mockRepository.findAllByGender(Gender.UNISEX)).thenReturn(new ArrayList<>());

        List<ViewProductDTO> unisexProducts;
        unisexProducts = toTest.getUnisexProducts();

        Assertions.assertEquals(0, unisexProducts.size());

        Product product = createProduct();
        product.setGender(Gender.UNISEX);
        when(mockRepository.findAllByGender(Gender.UNISEX)).thenReturn(List.of(product));

        unisexProducts = toTest.getUnisexProducts();

        Assertions.assertEquals(1, unisexProducts.size());

    }

    private Product createProduct() {
        Product product = new Product();
        product.setBrand("testBrand");
        product.setName("testName");
        product.setDescription("testDescription");
        product.setPrice(new BigDecimal("3.00"));
        product.setStock(5);
        product.setPackaging("80");
        product.setGender(Gender.FEMALE);

        Image image = new Image();
        image.setImage(new byte[]{1, 2, 3});
        product.setImage(image);

        Type type = new Type();
        type.setProductType(ProductType.PERFUME);
        product.setType(type);

        return product;
    }
}