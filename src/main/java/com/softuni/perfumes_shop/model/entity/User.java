package com.softuni.perfumes_shop.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String memberSince = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserDetail> userDetails = new ArrayList<>();

    @OneToOne(optional = false)
    private Cart cart;

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void addUserDetail(UserDetail userDetail) {
        this.userDetails.add(userDetail);
    }
}
