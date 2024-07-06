package com.softuni.perfumes_shop.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne(optional = false)
    private Customer customer;

    @ManyToOne(optional = false)
    private Address address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private double totalAmount;

    @Column(nullable = false)
    private String status;
}
