package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.model.dto.inbound.CreateOrderDTO;
import com.softuni.perfumes_shop.model.entity.*;
import com.softuni.perfumes_shop.model.enums.OrderStatus;
import com.softuni.perfumes_shop.repository.OrderRepository;
import com.softuni.perfumes_shop.service.*;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CurrentUserDetails currentUserDetails;
    private final ModelMapper modelMapper;
    private final CartService cartService;
    private final UserDetailService userDetailService;
    private final AddressService addressService;
    private final PaymentDetailService paymentDetailService;

    @Override
    @Transactional
    public void createOrder(CreateOrderDTO orderData) {
        Optional<User> optUser = currentUserDetails.optCurrentUser();
        if (optUser.isPresent()) {
            User user = optUser.get();

            UserDetail userDetail = mapUserDetail(orderData, user);
            Address address = mapAddress(orderData);
            PaymentDetail paymentDetail = mapPaymentDetail(orderData);

            Order order = new Order();

            List<OrderDetail> orderDetails = mapOrderDetails(user, order);
            order.setOrderDetails(orderDetails);
            order.setAddress(address);
            order.setPaymentDetail(paymentDetail);
            order.setUserDetail(userDetail);
            order.setOrderStatus(OrderStatus.CREATED);
            order.setTotalAmount(cartService.getTotalPrice());

            userDetailService.saveUserDetail(userDetail);
            addressService.saveAddress(address);
            paymentDetailService.savePaymentDetail(paymentDetail);
            orderRepository.save(order);

            cartService.emptyCart(user.getCart().getId());
        }
    }

    private UserDetail mapUserDetail(CreateOrderDTO orderData, User user) {

        Optional<UserDetail> optUserDetail = userDetailService.findUserDetailIfExists(orderData.getFirstName(), orderData.getLastName(), orderData.getPhoneNumber(), user.getId());

        if (optUserDetail.isPresent()) {
            return optUserDetail.get();
        }

        UserDetail userDetail = modelMapper.map(orderData, UserDetail.class);
        userDetail.setUser(user);

        return userDetail;
    }

    private Address mapAddress(CreateOrderDTO orderData) {

        Optional<Address> optAddress = addressService.findAddressIfExists(orderData.getCountry(), orderData.getCity(), orderData.getShippingAddress(), orderData.getPostalCode());

        if (optAddress.isPresent()) {
            return optAddress.get();
        }

        return modelMapper.map(orderData, Address.class);
    }

    private PaymentDetail mapPaymentDetail(CreateOrderDTO orderData) {

        Optional<PaymentDetail> optPaymentDetail = paymentDetailService.findPaymentDetailIfExists(orderData.getCardHolder(), orderData.getCardNumber(), orderData.getCardExpiration(), orderData.getSecurityCode());

        if (optPaymentDetail.isPresent()) {
            return optPaymentDetail.get();
        }

        return modelMapper.map(orderData, PaymentDetail.class);
    }

    private List<OrderDetail> mapOrderDetails(User user, Order order) {

        List<OrderDetail> orderDetails = user.getCart().getCartItems()
                .stream()
                .map(cartItem -> modelMapper.map(cartItem, OrderDetail.class))
                .toList();
        orderDetails.forEach(orderDetail -> orderDetail.setOrder(order));

        return orderDetails;
    }
}
