package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.init.Initializer;
import com.softuni.perfumes_shop.model.dto.inbound.AddProductDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ViewProductDTO;
import com.softuni.perfumes_shop.model.entity.Product;
import com.softuni.perfumes_shop.model.entity.Type;
import com.softuni.perfumes_shop.model.enums.ProductType;
import com.softuni.perfumes_shop.repository.ProductRepository;
import com.softuni.perfumes_shop.repository.TypeRepository;
import com.softuni.perfumes_shop.service.ProductService;
import com.softuni.perfumes_shop.service.exception.ObjectNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;



@SpringBootTest
public class ProductServiceImplIT {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TypeRepository typeRepository;

    @MockBean
    private Initializer initializer;

    private AddProductDTO productData;

    @BeforeEach
    public void setUp() {
        productData = createProductData();
        createType();
    }

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
        typeRepository.deleteAll();
    }


    @Test
    public void testAddProduct() throws IOException {

        productService.addProduct(productData);

        Assertions.assertEquals(1, productRepository.count());

        Optional<Product> optProduct = productRepository.findByName(productData.getName());

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        Assertions.assertEquals(productData.getBrand(), product.getBrand());
        Assertions.assertEquals(productData.getName(), product.getName());
        Assertions.assertEquals(productData.getDescription(), product.getDescription());
        Assertions.assertEquals(productData.getPrice(), product.getPrice());
        Assertions.assertEquals(productData.getStock(), product.getStock());
        Assertions.assertEquals(productData.getPackaging(), product.getPackaging());
        Assertions.assertEquals(productData.getProductTypeName(), product.getType().getProductType().getName());
        Assertions.assertArrayEquals(productData.getImage().getBytes(), product.getImage().getImage());
    }

    @Test
    public void testAddProductThrowsProductAlreadyExists() {
        productService.addProduct(productData);

        Assertions.assertThrows(NonUniqueResultException.class, () -> productService.addProduct(productData));
    }

    @Test
    public void testAddProductThrowsProductTypeDoesNotExists() {
        typeRepository.deleteAll();
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.addProduct(productData));
    }

    @Test
    public void testGetAllProducts() {

        productService.addProduct(productData);

        List<ViewProductDTO> viewProducts = productService.getAllProducts();

        Assertions.assertEquals(1, viewProducts.size());
        ViewProductDTO viewProduct = viewProducts.getFirst();
        Optional<Product> optProduct = productRepository.findByName(productData.getName());

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();
        Assertions.assertEquals(product.getId(), viewProduct.getId());
        Assertions.assertEquals(product.getBrand(), viewProduct.getBrand());
        Assertions.assertEquals(product.getName(), viewProduct.getName());
        Assertions.assertEquals(product.getDescription(), viewProduct.getDescription());
        Assertions.assertEquals(product.getPrice(), viewProduct.getPrice());
        Assertions.assertEquals(product.isInStock(), viewProduct.isInStock());
        Assertions.assertEquals(product.getPackaging(), viewProduct.getPackaging());
        Assertions.assertEquals(product.getType().getProductType().getName(), viewProduct.getProductTypeName());

    }

    @Test
    public void testGetViewProductById() {

        productService.addProduct(productData);

        Optional<Product> optProduct = productRepository.findByName(productData.getName());

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        ViewProductDTO viewProduct = productService.getViewProductById(product.getId());

        Assertions.assertEquals(product.getId(), viewProduct.getId());
        Assertions.assertEquals(product.getBrand(), viewProduct.getBrand());
        Assertions.assertEquals(product.getName(), viewProduct.getName());
        Assertions.assertEquals(product.getDescription(), viewProduct.getDescription());
        Assertions.assertEquals(product.getPrice(), viewProduct.getPrice());
        Assertions.assertEquals(product.isInStock(), viewProduct.isInStock());
        Assertions.assertEquals(product.getPackaging(), viewProduct.getPackaging());
        Assertions.assertEquals(product.getType().getProductType().getName(), viewProduct.getProductTypeName());
    }

    @Test
    public void testGetViewProductByIdThrowsProductNotFound() {

        Assertions.assertThrows(ObjectNotFoundException.class, () -> productService.getViewProductById(1L));
    }

    @Test
    public void testGetProductById() {

        productService.addProduct(productData);

        Optional<Product> optProduct = productRepository.findByName(productData.getName());

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        Product productById = productService.getProductById(product.getId());

        Assertions.assertEquals(product.getId(), productById.getId());
        Assertions.assertEquals(product.getBrand(), productById.getBrand());
        Assertions.assertEquals(product.getName(), productById.getName());
        Assertions.assertEquals(product.getDescription(), productById.getDescription());
        Assertions.assertEquals(product.getPrice(), productById.getPrice());
        Assertions.assertEquals(product.getStock(), productById.getStock());
        Assertions.assertEquals(product.getPackaging(), productById.getPackaging());
        Assertions.assertEquals(product.getType().getProductType().getName(), productById.getType().getProductType().getName());
        Assertions.assertArrayEquals(product.getImage().getImage(), productById.getImage().getImage());
    }

    @Test
    public void testDeleteProductById() {

        productService.addProduct(productData);

        Optional<Product> optProduct = productRepository.findByName(productData.getName());

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        productService.deleteProductById(product.getId());

        Assertions.assertEquals(0, productRepository.count());
    }

    @Test
    public void testFindNameOfProduct() {

        productService.addProduct(productData);

        Optional<Product> optProduct = productRepository.findByName(productData.getName());

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        String name = productService.findNameOfProduct(product.getId());

        Assertions.assertEquals(product.getName(), name);
    }

    @Test
    public void testFindNameOfProductIsNull() {
        String name = productService.findNameOfProduct(1L);

        Assertions.assertNull(name);
    }

    private AddProductDTO createProductData() {
        AddProductDTO productData = new AddProductDTO();
        productData.setBrand("testBrand");
        productData.setName("testName");
        productData.setDescription("testDescription");
        productData.setPrice(new BigDecimal("3.00"));
        productData.setStock(5);
        productData.setPackaging("80");
        productData.setProductTypeName("Perfume");
        productData.setImage(new MockMultipartFile("testImage", "testImage.jpg", "image/jpeg", "image/jpeg".getBytes()));
        return productData;
    }

    private void createType() {
        Type type = new Type();
        type.setProductType(ProductType.PERFUME);
        type.setDescription("testDescription");
        typeRepository.save(type);
    }
}