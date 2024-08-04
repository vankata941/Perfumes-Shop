package com.softuni.perfumes_shop.model.entity;

import com.softuni.perfumes_shop.model.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String brand;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private String packaging;

    @ManyToOne(optional = false)
    private Type type;

    @OneToOne(cascade = CascadeType.ALL)
    private Image image;

    public boolean isInStock() {
        return this.stock > 0;
    };

}

