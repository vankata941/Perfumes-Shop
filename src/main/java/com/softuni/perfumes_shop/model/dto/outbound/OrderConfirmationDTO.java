package com.softuni.perfumes_shop.model.dto.outbound;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class OrderConfirmationDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String orderCreatedDate;

}
