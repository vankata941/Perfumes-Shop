package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.model.dto.UserLoginDTO;
import com.softuni.perfumes_shop.model.dto.UserProfileDTO;
import com.softuni.perfumes_shop.model.dto.UserRegisterDTO;
import com.softuni.perfumes_shop.service.UserService;
import jakarta.persistence.NonUniqueResultException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @ModelAttribute("registerData")
    private UserRegisterDTO registerData() {
        return new UserRegisterDTO();
    }

    @ModelAttribute("loginData")
    private UserLoginDTO loginData() {
        return new UserLoginDTO();
    }

    @GetMapping("/register")
    public String viewRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(
                            @Valid UserRegisterDTO registerData,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerData", registerData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);

            return "redirect:/users/register";
        }

        try {

            userService.register(registerData);

        } catch (NonUniqueResultException e) {
            redirectAttributes.addFlashAttribute("registerData", registerData);
            redirectAttributes.addFlashAttribute("usernameOrEmailTaken", true);

            return "redirect:/users/register";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("registerData", registerData);
            redirectAttributes.addFlashAttribute("passwordMismatch", true);

            return "redirect:/users/register";
        }

        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String viewLogin() {
        return "login";
    }

    @GetMapping("/login-error")
    public String viewLoginError(Model model) {
        model.addAttribute("errorMessage", true);
        model.addAttribute("loginData", new UserLoginDTO());

        return "login";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        UserProfileDTO profile= userService.getProfileData();
        model.addAttribute("profileData", profile);

        return "user-profile";
    }
}
