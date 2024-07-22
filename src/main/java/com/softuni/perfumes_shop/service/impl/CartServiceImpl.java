package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.model.dto.outbound.ViewCartItemDTO;
import com.softuni.perfumes_shop.model.entity.Cart;
import com.softuni.perfumes_shop.model.entity.CartItem;
import com.softuni.perfumes_shop.model.entity.Product;
import com.softuni.perfumes_shop.model.entity.User;
import com.softuni.perfumes_shop.repository.CartRepository;
import com.softuni.perfumes_shop.service.CartItemService;
import com.softuni.perfumes_shop.service.CartService;
import com.softuni.perfumes_shop.service.ProductService;
import com.softuni.perfumes_shop.service.exception.ObjectNotFoundException;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectDeletedException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CurrentUserDetails currentUserDetails;
    private final ModelMapper modelMapper;
    private final CartItemService cartItemService;

    @Override
    @Transactional
    public boolean addItemToCart(Long id) {
        Optional<User> optUser = currentUserDetails.optCurrentUser();
        if (optUser.isPresent()) {
            User user = optUser.get();
            Cart cart = user.getCart();

            Optional<Product> optProduct = cart.getCartItems()
                    .stream()
                    .map(CartItem::getProduct)
                    .filter(p -> Objects.equals(p.getId(), id)).findAny();

            if (optProduct.isPresent()) {
                return false;
            }

            CartItem cartItem = new CartItem();
            cartItem.setProduct(productService.getProductById(id));
            cartItem.setCart(cart);

            cart.addCartItem(cartItem);

            cartRepository.save(cart);

            return true;
        }
        return false;
    }

    @Override
    public void addNewCart(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public List<ViewCartItemDTO> getAllCartItems() {
        Optional<User> optUser = currentUserDetails.optCurrentUser();
        if (optUser.isPresent()) {
            User user = optUser.get();
            Cart cart = user.getCart();
            List<CartItem> cartItems = cart.getCartItems();
            List<ViewCartItemDTO> viewCartItemDTOS = new ArrayList<>();
            cartItems.stream().map(this::mapProduct).forEach(viewCartItemDTOS::add);

            return viewCartItemDTOS;
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public BigDecimal getSubtotal() {
        Optional<User> optUser = currentUserDetails.optCurrentUser();
        if (optUser.isPresent()) {
            User user = optUser.get();
            Cart cart = user.getCart();
            List<CartItem> cartItems = cart.getCartItems();
            List<BigDecimal> cartItemPrices = cartItems.stream().map(cartItem -> {
                BigDecimal price = cartItem.getProduct().getPrice();
                int quantity = cartItem.getQuantity();
                return price.multiply(BigDecimal.valueOf(quantity));
            }).toList();

            BigDecimal subtotal = BigDecimal.ZERO;

            for (BigDecimal cartItemPrice : cartItemPrices) {
                subtotal = subtotal.add(cartItemPrice);
            }

            return subtotal;
        }
        return BigDecimal.ZERO;
    }

    @Override
    @Transactional
    public void removeCartItem(Long id) {
        Optional<User> optUser = currentUserDetails.optCurrentUser();
        if (optUser.isPresent()) {
            User user = optUser.get();
            Cart cart = user.getCart();

            boolean isCartItemPresent = cart.getCartItems().stream().map(CartItem::getId).anyMatch(cartItemId -> cartItemId.equals(id));

            if (isCartItemPresent) {
                cart.getCartItems().removeIf(cartItem -> cartItem.getId().equals(id));
                cartRepository.save(cart);
                cartItemService.removeById(id);
            } else {
                throw new ObjectNotFoundException("There isn't a product with id " + id + " in your shopping cart!");
            }
        }
    }

    @Override
    @Transactional
    public void changeQuantityById(Long id, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero!");
        }
        if (quantity != 1) {
            Optional<User> optUser = currentUserDetails.optCurrentUser();
            if (optUser.isPresent()) {
                User user = optUser.get();
                Cart cart = user.getCart();
                for (CartItem cartItem : cart.getCartItems()) {
                    if (Objects.equals(cartItem.getId(), id)) {
                        cartItem.setQuantity(quantity);
                    }
                }
                cartRepository.save(cart);
            }
        }

    }

    private ViewCartItemDTO mapProduct(CartItem cartItem) {
        ViewCartItemDTO viewCartItemDTO = modelMapper.map(cartItem.getProduct(), ViewCartItemDTO.class);
        if (cartItem.getProduct().getImage() != null) {
            viewCartItemDTO.setImage(Base64.getEncoder().encodeToString(cartItem.getProduct().getImage().getImage()));
        }
        viewCartItemDTO.setId(cartItem.getId());
        viewCartItemDTO.setQuantity(cartItem.getQuantity());

        return viewCartItemDTO;
    }
}
