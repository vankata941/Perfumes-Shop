package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.dto.inbound.CreateOrderDTO;

public interface OrderService {

    void createOrder(CreateOrderDTO orderData);

}
