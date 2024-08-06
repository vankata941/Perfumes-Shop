package com.softuni.perfumes_shop.model.dto.inbound;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class AddCarouselImageDTO {

    @NotNull
    private MultipartFile image;
}
