package com.softuni.perfumes_shop.model.dto.inbound;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AddProductDTO {

    @NotBlank
    @Size(min = 1, max = 30)
    private String brand;

    @NotBlank
    @Size(min = 1, max = 30)
    private String name;

    @NotBlank
    @Size(min = 20, max = 300)
    private String description;

    @NotNull
    @Positive
    @DecimalMin(value = "0.01")
    private BigDecimal price;

    @NotNull
    @Positive
    @Min(value = 1)
    private Integer stock;

    @NotNull
    private String productTypeName;

    @NotNull
    private MultipartFile image;
}
