package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.init.Initializer;
import com.softuni.perfumes_shop.model.entity.Image;
import com.softuni.perfumes_shop.model.entity.Product;
import com.softuni.perfumes_shop.model.entity.Type;
import com.softuni.perfumes_shop.model.enums.Gender;
import com.softuni.perfumes_shop.model.enums.ImageType;
import com.softuni.perfumes_shop.model.enums.ProductType;
import com.softuni.perfumes_shop.repository.ImageRepository;
import com.softuni.perfumes_shop.repository.ProductRepository;
import com.softuni.perfumes_shop.repository.TypeRepository;
import com.softuni.perfumes_shop.service.JwtService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerIT {

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private Initializer initializer;

    @Autowired
    private MockMvc mockMvc;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = createProduct();
    }

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
        typeRepository.deleteAll();
        imageRepository.deleteAll();
    }

    @Test
    public void testSearch() throws Exception {
        MvcResult result = mockMvc.perform(get("/search")
                        .param("keyword", "test"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertThat(content).contains(product.getId().toString());
        Assertions.assertThat(content).contains(product.getName());
        Assertions.assertThat(content).contains(product.getPrice().toString());
        Assertions.assertThat(content).contains(product.getType().getProductType().getName());
    }
    private Product createProduct() {

        Image image = new Image();
        image.setName("img");
        image.setImageType(ImageType.PRODUCT_IMAGE);
        image.setImage(new byte[]{1, 2, 3});

        Type type = new Type();
        type.setProductType(ProductType.PERFUME);
        type.setDescription("testDescription");
        typeRepository.save(type);


        Product product = new Product();
        product.setBrand("testBrand");
        product.setName("testName");
        product.setDescription("testDescription");
        product.setPrice(new BigDecimal("3.00"));
        product.setStock(5);
        product.setPackaging("80");
        product.setGender(Gender.MALE);
        product.setImage(image);
        product.setType(type);

        productRepository.save(product);

        return product;
    }
}