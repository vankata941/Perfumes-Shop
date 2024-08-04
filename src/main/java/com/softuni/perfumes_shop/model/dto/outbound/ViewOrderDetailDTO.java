package com.softuni.perfumes_shop.model.dto.outbound;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViewOrderDetailDTO {

    private String brand;

    private String name;

    private int quantity;

    private String packaging;

    private String gender;

    private String image;
}
