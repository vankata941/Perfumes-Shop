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
@Table(name = "payment_details")
public class PaymentDetail extends BaseEntity {

    @Column(nullable = false)
    private String cardHolder;

    @Column(unique = true, nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String cardExpiration;

    @Column(nullable = false)
    private String securityCode;


}
