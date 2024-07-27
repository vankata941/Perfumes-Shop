package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.dto.inbound.CreateOrderDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ViewOrderDTO;
import com.softuni.perfumes_shop.model.entity.Order;

import java.util.List;

public interface OrderService {

    void createOrder(CreateOrderDTO orderData);

    List<ViewOrderDTO> getAllOrdersByUser();
}
