package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.model.entity.OrderDetail;
import com.softuni.perfumes_shop.repository.OrderDetailRepository;
import com.softuni.perfumes_shop.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    @Override
    public void saveOrderDetail(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }
}
