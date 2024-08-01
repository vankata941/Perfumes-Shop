package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.dto.inbound.CreateOrderDTO;
import com.softuni.perfumes_shop.model.dto.outbound.OrderConfirmationDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ViewOrderDTO;
import com.softuni.perfumes_shop.model.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    void createOrder(CreateOrderDTO orderData);

    List<ViewOrderDTO> getAllOrdersByUser();

    List<OrderConfirmationDTO> getAllOrders();

    boolean changeStatusById(Long id);

    ViewOrderDTO getViewOrderDTOByOrderId(Long id);

    void deleteOrderById(Long id);

    Optional<Order> getOrderById(Long id);
}
