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
@Table(name = "cart_items")
public class CartItem extends BaseEntity {

    @ManyToOne(optional = false)
    private Cart cart;

    @ManyToOne(optional = false)
    private Product product;

    @Column
    private int quantity;
}
