package com.softuni.perfumes_shop.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_details")
public class OrderDetail extends BaseEntity {

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;
}
