package com.softuni.perfumes_shop.model.dto.outgoing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserProfileDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String memberSince;
}
