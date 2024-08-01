package com.softuni.perfumes_shop.model.dto.inbound;

import com.softuni.perfumes_shop.model.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViewShippingDetailDTO {

    private Long orderId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String orderCreatedDate;

    private OrderStatus status;

}
