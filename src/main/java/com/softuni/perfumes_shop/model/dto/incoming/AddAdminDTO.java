package com.softuni.perfumes_shop.model.dto.incoming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddAdminDTO {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

}
