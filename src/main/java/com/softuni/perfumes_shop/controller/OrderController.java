package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.model.dto.inbound.CreateOrderDTO;
import com.softuni.perfumes_shop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @ModelAttribute("orderData")
    private CreateOrderDTO orderData() {
        return new CreateOrderDTO();
    }

    @GetMapping("/create")
    public String viewCreateOrder() {
        return "create-order";
    }

    @PostMapping("/create")
    public String doCreateOrder(
            @Valid CreateOrderDTO orderData,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("orderData", orderData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.orderData", bindingResult);

            return "redirect:/order/create";
        }

        orderService.createOrder(orderData);

        return "create-order";
    }
}
