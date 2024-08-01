package com.softuni.perfumes_shop.repository;

import com.softuni.perfumes_shop.model.entity.Order;
import com.softuni.perfumes_shop.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserDetailId(Long id);

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);
}
