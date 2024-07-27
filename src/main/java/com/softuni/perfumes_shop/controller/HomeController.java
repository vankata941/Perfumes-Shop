package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.model.dto.inbound.AddCardDTO;
import com.softuni.perfumes_shop.service.ImageService;
import com.softuni.perfumes_shop.service.exception.AuthorizationCheckException;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import jakarta.persistence.NonUniqueResultException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ImageService imageService;
    private final CurrentUserDetails currentUserDetails;

    @ModelAttribute("cardData")
    private AddCardDTO cardData() {
        return new AddCardDTO();
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contacts")
    public String contacts() {
        return "contacts";
    }

    @GetMapping("/card/add")
    public String viewAddCard() {
        return "add-card";
    }

    @PostMapping("/card/add")
    public String doAddCard(
            @Valid AddCardDTO cardData,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (!currentUserDetails.hasRole("ADMIN")) {
            throw new AuthorizationCheckException();
        }

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("cardData", cardData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.cardData", bindingResult);

            return "redirect:/card/add";
        }

            try {
                imageService.uploadImage(cardData.getImage());
            } catch (NonUniqueResultException | IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("cardData", cardData);
                redirectAttributes.addFlashAttribute("hasError", true);
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

                return "redirect:/card/add";
            }


        return "index";
    }
}
