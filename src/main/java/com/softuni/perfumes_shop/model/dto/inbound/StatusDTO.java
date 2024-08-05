package com.softuni.perfumes_shop.model.dto.inbound;

import com.softuni.perfumes_shop.model.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatusDTO {

    private Long orderId;

    private OrderStatus status;

}
