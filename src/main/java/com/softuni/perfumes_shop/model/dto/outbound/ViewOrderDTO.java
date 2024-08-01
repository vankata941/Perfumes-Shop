package com.softuni.perfumes_shop.model.dto.outbound;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ViewOrderDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String orderStatus;

    private BigDecimal totalAmount;

    private String orderCreatedDate;

    private List<ViewOrderDetailDTO> orderDetails;
}
