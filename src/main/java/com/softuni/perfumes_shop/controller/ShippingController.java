package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.service.ShippingService;
import com.softuni.perfumes_shop.service.exception.AuthorizationCheckException;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;
    private final CurrentUserDetails currentUserDetails;


    @GetMapping("/shipping")
    public String viewOrderShipments(Model model) {

        if (!currentUserDetails.hasRole("ADMIN")) {
            throw new AuthorizationCheckException();
        }

        model.addAttribute("shipping", shippingService.getAllShipments());

        return "monitor-shipping";
    }
}
