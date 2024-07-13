package com.softuni.perfumes_shop.model.entity;

import com.softuni.perfumes_shop.model.enums.PerfumeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "types")
public class Type extends BaseEntity {

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private PerfumeType perfumeType;

    @Column(nullable = false)
    private String description;
}
