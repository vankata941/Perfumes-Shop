package com.softuni.perfumes_shop.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "customer")
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    @OneToOne
    private Cart cart;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String paymentMethod;
}

