package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.dto.inbound.AddProductDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ViewProductDTO;
import com.softuni.perfumes_shop.model.entity.Product;


import java.util.List;

public interface ProductService {

    void addProduct(AddProductDTO productData);

    List<ViewProductDTO> getAllProducts();

    Product getProductById(Long id);

    ViewProductDTO getViewProductById(Long id);

    void deleteProductById(Long id);

    String findNameOfProduct(Long id);

    List<ViewProductDTO> searchProducts(String keyword);

    List<ViewProductDTO> getMaleProducts();

    List<ViewProductDTO> getFemaleProducts();

    List<ViewProductDTO> getUnisexProducts();
}
