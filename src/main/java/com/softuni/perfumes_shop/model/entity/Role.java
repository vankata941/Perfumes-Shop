package com.softuni.perfumes_shop.model.entity;

import com.softuni.perfumes_shop.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
