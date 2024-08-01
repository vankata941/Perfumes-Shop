package com.softuni.perfumes_shop.model.dto.outbound;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShippingAddressDTO {

    private String country;

    private String city;

    private String shippingAddress;

    private String postalCode;
}
