package com.softuni.perfumes_shop.model.dto.inbound;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateOrderDTO {

    @NotBlank
    @Size(min = 2, max = 20)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 20)
    private String lastName;

    @NotBlank
    private String shippingAddress;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotBlank
    @Size(min = 4, max = 4)
    private String postalCode;

    @NotBlank
    private String cardHolder;

    @NotBlank
    @Size(min = 19, max = 19)
    private String cardNumber;

    @NotBlank
    @Size(min = 5, max = 5)
    private String cardExpiration;

    @NotBlank
    @Size(min = 3, max = 3)
    private String securityCode;
}
