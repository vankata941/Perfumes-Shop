package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.repository.CartItemRepository;
import com.softuni.perfumes_shop.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;


    @Override
    public void removeById(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public void deleteById(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
