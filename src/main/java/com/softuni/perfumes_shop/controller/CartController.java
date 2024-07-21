package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.model.dto.outbound.ViewCartItemDTO;
import com.softuni.perfumes_shop.service.CartService;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CurrentUserDetails currentUserDetails;

    @GetMapping
    public String viewCart(Model model) {
        if (!currentUserDetails.isAuthenticated()) {
            return "redirect:/login";
        }
        List<ViewCartItemDTO> cartItems = cartService.getAllCartItems();
        model.addAttribute("cartItems", cartItems);

        BigDecimal subtotal = cartService.getSubtotal();
        model.addAttribute("subtotal", subtotal);

        BigDecimal shipping = BigDecimal.ZERO;
        BigDecimal totalPrice = BigDecimal.ZERO;

        if (!Objects.equals(subtotal, BigDecimal.ZERO)) {
            shipping = BigDecimal.valueOf(10);
            totalPrice = subtotal.add(shipping);
        }
        model.addAttribute("shipping", shipping);
        model.addAttribute("totalPrice", totalPrice);

        return "cart";
    }

    @PostMapping("/add-product/{id}")
    public String addProduct(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        if (!currentUserDetails.isAuthenticated()) {
            return "redirect:/login";
        }
        boolean isAdded = cartService.addItemToCart(id);

        if (isAdded) {
            redirectAttributes.addFlashAttribute("isAdded", true);
        } else {
            redirectAttributes.addFlashAttribute("isNotAdded", true);
        }

        return "redirect:/products/product/{id}";
    }

    @DeleteMapping("/delete/cart-item/{id}")
    public String removeCartItem(@PathVariable Long id) {
        if (!currentUserDetails.isAuthenticated()) {
            return "redirect:/login";
        }

        cartService.removeCartItem(id);

        return "redirect:/cart";
    }
}
