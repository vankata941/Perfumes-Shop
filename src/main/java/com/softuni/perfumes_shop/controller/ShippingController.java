package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.service.ShippingService;
import com.softuni.perfumes_shop.service.exception.AuthorizationCheckException;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;
    private final CurrentUserDetails currentUserDetails;


    @GetMapping
    public String viewOrderShipments(Model model) {

        if (!currentUserDetails.hasRole("ADMIN")) {
            throw new AuthorizationCheckException();
        }

        model.addAttribute("shipping", shippingService.getAllShipping());

        return "monitor-shipping";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteShipping(@PathVariable Long id) {
        if (!currentUserDetails.hasRole("ADMIN")) {
            throw new AuthorizationCheckException();
        }

        shippingService.deleteShippingByOrderId(id);

        return "redirect:/shipping";
    }
}
