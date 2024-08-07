package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.model.dto.inbound.UserChangePasswordDTO;
import com.softuni.perfumes_shop.model.dto.inbound.UserLoginDTO;
import com.softuni.perfumes_shop.model.dto.outbound.UserProfileDTO;
import com.softuni.perfumes_shop.model.dto.inbound.UserRegisterDTO;
import com.softuni.perfumes_shop.service.UserService;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import jakarta.persistence.NonUniqueResultException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final CurrentUserDetails currentUserDetails;

    @ModelAttribute("registerData")
    private UserRegisterDTO registerData() {
        return new UserRegisterDTO();
    }

    @ModelAttribute("loginData")
    private UserLoginDTO loginData() {
        return new UserLoginDTO();
    }

    @ModelAttribute("changePasswordData")
    private UserChangePasswordDTO changePasswordData() {
        return new UserChangePasswordDTO();
    }

    @GetMapping("/register")
    public String viewRegister() {
        if (currentUserDetails.isAuthenticated()) {
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(
                            @Valid UserRegisterDTO registerData,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes
    ) {
        if (currentUserDetails.isAuthenticated()) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerData", registerData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);

            return "redirect:/user/register";
        }

        try {

            userService.register(registerData);

        } catch (NonUniqueResultException e) {
            redirectAttributes.addFlashAttribute("registerData", registerData);
            redirectAttributes.addFlashAttribute("usernameOrEmailTaken", true);

            return "redirect:/user/register";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("registerData", registerData);
            redirectAttributes.addFlashAttribute("passwordMismatch", true);

            return "redirect:/user/register";
        }

        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String viewLogin() {
        if (currentUserDetails.isAuthenticated()) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/login-error")
    public String viewLoginError(Model model) {
        if (currentUserDetails.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("hasError", true);
        model.addAttribute("loginData", new UserLoginDTO());

        return "login";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        UserProfileDTO profile = userService.getProfileData();
        model.addAttribute("profileData", profile);

        return "user-profile";
    }

    @GetMapping("/change-password")
    public String viewChangePassword() {
        return "change-password";
    }

    @PostMapping("/change-password")
    public String doChangePassword(
            @Valid UserChangePasswordDTO changePasswordData,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("changePasswordData", changePasswordData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changePasswordData", bindingResult);

            return "redirect:/user/change-password";
        }

        try {
            userService.changePassword(changePasswordData, request, response);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("changePasswordData", changePasswordData);
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/user/change-password";
        }

        return "change-password-redirect";
    }
}
