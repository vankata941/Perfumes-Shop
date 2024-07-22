package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.dto.outbound.ViewCartItemDTO;
import com.softuni.perfumes_shop.model.entity.Cart;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {

    boolean addItemToCart(Long id);

    void addNewCart(Cart cart);

    List<ViewCartItemDTO> getAllCartItems();

    BigDecimal getSubtotal();

    void removeCartItem(Long id);

    void changeQuantityById(Long id, int quantity);
}
