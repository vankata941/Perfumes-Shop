package com.softuni.perfumes_shop.model.entity;

import com.softuni.perfumes_shop.model.enums.ImageType;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ImageType imageType;
}
