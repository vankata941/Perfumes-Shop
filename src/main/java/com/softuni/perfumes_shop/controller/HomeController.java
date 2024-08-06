package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.model.dto.inbound.AddCarouselImageDTO;
import com.softuni.perfumes_shop.model.dto.outbound.CarouselDTO;
import com.softuni.perfumes_shop.model.enums.ImageType;
import com.softuni.perfumes_shop.service.ImageService;
import com.softuni.perfumes_shop.service.exception.AuthorizationCheckException;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import jakarta.persistence.NonUniqueResultException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ImageService imageService;
    private final CurrentUserDetails currentUserDetails;

    @ModelAttribute("carouselData")
    private AddCarouselImageDTO carouselData() {
        return new AddCarouselImageDTO();
    }

    @GetMapping("/")
    public String home(Model model) {
        List<CarouselDTO> carouselImages = imageService.getCarousels();
        model.addAttribute("carouselImages", carouselImages);

        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contacts")
    public String contacts() {
        return "contacts";
    }

    @GetMapping("/terms")
    public String terms() {
        return "terms";
    }

    @GetMapping("/carousel/add")
    public String viewAddCarousel() {
        return "carousel-add";
    }

    @PostMapping("/carousel/add")
    public String doAddCarousel(
            @Valid AddCarouselImageDTO carouselData,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (!currentUserDetails.hasRole("ADMIN")) {
            throw new AuthorizationCheckException();
        }

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("carouselData", carouselData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.carouselData", bindingResult);

            return "redirect:/carousel/add";
        }

        try {
            imageService.uploadImage(carouselData.getImage(), ImageType.CAROUSEL_IMAGE);
        } catch (NonUniqueResultException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("cardData", carouselData);
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/carousel/add";
        }


        return "redirect:/carousel/manage";
    }

    @GetMapping("/carousel/manage")
    public String viewManageCarousel(Model model) {
        List<CarouselDTO> carousels = imageService.getCarousels();
        model.addAttribute("carousels", carousels);

        return "carousel-manage";
    }

    @DeleteMapping("/carousel/remove/{name}")
    public String removeCarousel(@PathVariable String name) {
        imageService.deleteImage(name);

        return "redirect:/carousel/manage";
    }
}
