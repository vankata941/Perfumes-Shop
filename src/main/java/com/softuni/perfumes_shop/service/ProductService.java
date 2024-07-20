package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.dto.inbound.AddProductDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ViewProductDTO;


import java.util.List;

public interface ProductService {

    void addProduct(AddProductDTO productData);

    List<ViewProductDTO> getAllProducts();

    ViewProductDTO getProductById(Long id);

    void deleteProductById(Long id);

    String findNameOfProduct(Long id);

}
