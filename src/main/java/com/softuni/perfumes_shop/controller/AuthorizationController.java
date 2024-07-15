package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.model.dto.incoming.AddAdminDTO;
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

@Controller
@RequiredArgsConstructor
public class AuthorizationController {

    private final UserService userService;
    private final CurrentUserDetails currentUserDetails;

    @ModelAttribute("newAdminData")
    private AddAdminDTO newAdminData() {
        return new AddAdminDTO();
    }

    @GetMapping("/add-admin")
    public String viewAddAdmin() {
        if (!currentUserDetails.hasRole("ADMIN")) {
            throw new AuthorizationCheckException();
        }
        return "add-admin";
    }

    @PostMapping("/add-admin")
    public String doAddAdmin(@Valid AddAdminDTO newAdminData,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model
    ) {
        if (!currentUserDetails.hasRole("ADMIN")) {
            throw new AuthorizationCheckException();
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("newAdminData", newAdminData);

            return "redirect:/add-admin";
        }

        try {
            userService.grantAuthorizationAdmin(newAdminData);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("newAdminData", newAdminData);
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/add-admin";
        }

        model.addAttribute("username", newAdminData.getUsername());

        return "add-admin-redirect";
    }
}
