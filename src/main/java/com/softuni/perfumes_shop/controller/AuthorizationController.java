package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.model.dto.incoming.AddAuthorizationDTO;
import com.softuni.perfumes_shop.model.enums.UserRole;
import com.softuni.perfumes_shop.service.UserService;
import com.softuni.perfumes_shop.service.exception.AuthorizationCheckException;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class AuthorizationController {

    private final UserService userService;
    private final CurrentUserDetails currentUserDetails;

    @ModelAttribute("authorizationData")
    private AddAuthorizationDTO authorizationData() {
        return new AddAuthorizationDTO();
    }

    @GetMapping("/authorize")
    public String viewAuthorize(Model model) {
        if (!currentUserDetails.hasRole("ADMIN")) {
            throw new AuthorizationCheckException();
        }

        model.addAttribute("userRoles",
                Arrays.stream(UserRole.values())
                .map(UserRole::getName)
                .filter(ur -> !ur.equals("User"))
                .toArray());

        return "authorize";
    }

    @PostMapping("/authorize")
    public String doAuthorize(@Valid AddAuthorizationDTO authorizationData,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model
    ) {
        if (!currentUserDetails.hasRole("ADMIN")) {
            throw new AuthorizationCheckException();
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("authorizationData", authorizationData);

            return "redirect:/authorize";
        }

        try {
            userService.grantAuthorizationAdmin(authorizationData);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("authorizationData", authorizationData);
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/authorize";
        }

        model.addAttribute("authorizationData", authorizationData);

        return "authorize-redirect";
    }
}
