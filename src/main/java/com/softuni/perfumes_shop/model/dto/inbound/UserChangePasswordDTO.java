package com.softuni.perfumes_shop.model.dto.inbound;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserChangePasswordDTO {

    @NotBlank
    @Size(min = 8, max = 20)
    private String currentPassword;

    @NotBlank
    @Size(min = 8, max = 20)
    private String newPassword;

    @NotBlank
    @Size(min = 8, max = 20)
    private String confirmNewPassword;
}
