package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.dto.incoming.AddProductDTO;
import com.softuni.perfumes_shop.model.dto.outgoing.ViewProductDTO;


import java.util.List;

public interface ProductService {

    void addProduct(AddProductDTO productData);

    List<ViewProductDTO> getAllProducts();
}
