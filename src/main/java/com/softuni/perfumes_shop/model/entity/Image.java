package com.softuni.perfumes_shop.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "images")
public class Image extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "BLOB")
    private byte[] image;
}
