package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.model.dto.inbound.CreateOrderDTO;
import com.softuni.perfumes_shop.model.dto.outbound.OrderConfirmationDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ViewOrderDTO;
import com.softuni.perfumes_shop.service.OrderService;
import com.softuni.perfumes_shop.service.ShippingService;
import com.softuni.perfumes_shop.service.exception.AuthorizationCheckException;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CurrentUserDetails currentUserDetails;
    private final ShippingService shippingService;

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

        return "redirect:/order/list";
    }

    @GetMapping("/list")
    public String listOrders(Model model) {

        List<ViewOrderDTO> orders = orderService.getAllOrdersByUser();

        model.addAttribute("orders", orders);

        return "my-orders";
    }

    @GetMapping("/confirm")
    public String viewConfirmOrder(Model model) {

        if (!currentUserDetails.hasRole("ADMIN")) {
            throw new AuthorizationCheckException();
        }

        List<OrderConfirmationDTO> ordersToConfirm = orderService.getAllOrders();

        model.addAttribute("ordersToConfirm", ordersToConfirm);

        return "confirm-order";
    }

    @PostMapping("/confirm/{id}")
    public String doConfirmOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        if (!currentUserDetails.hasRole("ADMIN")) {
            throw new AuthorizationCheckException();
        }

        boolean hasChanged = orderService.changeStatusById(id);

        shippingService.createShipping(id);

        if (hasChanged) {
            redirectAttributes.addFlashAttribute("statusChanged", true);

            return "redirect:/order/confirm";
        }

        return "confirm-order";
    }

    @GetMapping("/details/{id}")
    public String viewOrderDetails(@PathVariable Long id, Model model) {
        if (!currentUserDetails.hasRole("ADMIN")) {
            throw new AuthorizationCheckException();
        }

        ViewOrderDTO viewOrderData = orderService.getViewOrderDTOByOrderId(id);

        model.addAttribute("viewOrderData", viewOrderData);

        return "order-details";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        if (!currentUserDetails.hasRole("ADMIN")) {
            throw new AuthorizationCheckException();
        }

        orderService.deleteOrderById(id);

        return "redirect:/order/confirm";
    }
}
