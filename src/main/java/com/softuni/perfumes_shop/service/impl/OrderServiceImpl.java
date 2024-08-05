package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.model.dto.inbound.CreateOrderDTO;
import com.softuni.perfumes_shop.model.dto.outbound.OrderConfirmationDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ViewOrderDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ViewOrderDetailDTO;
import com.softuni.perfumes_shop.model.entity.*;
import com.softuni.perfumes_shop.model.enums.OrderStatus;
import com.softuni.perfumes_shop.repository.OrderRepository;
import com.softuni.perfumes_shop.service.*;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Base64;
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

    @Override
    @Transactional
    public List<ViewOrderDTO> getAllOrdersByUser() {
        List<ViewOrderDTO> viewOrders = new ArrayList<>();

        Optional<User> optUser = currentUserDetails.optCurrentUser();
        if (optUser.isPresent()) {
            List<UserDetail> userDetails = optUser.get().getUserDetails();

            for (UserDetail userDetail : userDetails) {
                List<Order> allByUserDetailId = orderRepository.findAllByUserDetailId(userDetail.getId());
                for (Order order : allByUserDetailId) {
                    ViewOrderDTO viewOrderDTO = getViewOrderDTO(order);

                    viewOrders.add(viewOrderDTO);
                }
            }
        }

        return viewOrders;
    }

    private ViewOrderDTO getViewOrderDTO(Order order) {
        ViewOrderDTO viewOrderDTO = new ViewOrderDTO();
        viewOrderDTO.setId(order.getId());
        viewOrderDTO.setFirstName(order.getUserDetail().getFirstName());
        viewOrderDTO.setLastName(order.getUserDetail().getLastName());
        viewOrderDTO.setOrderStatus(order.getOrderStatus().getName());
        viewOrderDTO.setTotalAmount(order.getTotalAmount());
        viewOrderDTO.setOrderCreatedDate(order.getOrderCreatedDate());

        List<ViewOrderDetailDTO> viewOrderDetailList = mapOrderDetailDTO(order);
        viewOrderDTO.setOrderDetails(viewOrderDetailList);
        return viewOrderDTO;
    }

    @Override
    public List<OrderConfirmationDTO> getAllOrders() {
        List<Order> allByOrderStatus = orderRepository.findAllByOrderStatus(OrderStatus.CREATED);

        List<OrderConfirmationDTO> orderConfirmationsList = new ArrayList<>();

        for (Order order : allByOrderStatus) {
            OrderConfirmationDTO orderConfirmationDTO = new OrderConfirmationDTO();
            orderConfirmationDTO.setId(order.getId());
            orderConfirmationDTO.setFirstName(order.getUserDetail().getFirstName());
            orderConfirmationDTO.setLastName(order.getUserDetail().getLastName());
            orderConfirmationDTO.setPhoneNumber(order.getUserDetail().getPhoneNumber());
            orderConfirmationDTO.setOrderCreatedDate(order.getOrderCreatedDate());

            orderConfirmationsList.add(orderConfirmationDTO);
        }

        return orderConfirmationsList;
    }

    @Override
    public boolean changeStatusById(Long id) {
        Optional<Order> optOrder = orderRepository.findById(id);

        if (optOrder.isPresent()) {
            optOrder.get().setOrderStatus(OrderStatus.CONFIRMED);

            orderRepository.save(optOrder.get());
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public ViewOrderDTO getViewOrderDTOByOrderId(Long id) {
        ViewOrderDTO viewOrderDTO;
        Optional<Order> optOrder = orderRepository.findById(id);

        if (optOrder.isPresent()) {
            Order order = optOrder.get();
            viewOrderDTO = getViewOrderDTO(order);
        } else {
            viewOrderDTO = new ViewOrderDTO();
        }

        return viewOrderDTO;
    }

    @Override
    public void deleteOrderById(Long id) {
        Optional<Order> optOrder = orderRepository.findById(id);
        if (optOrder.isPresent()) {
            if (optOrder.get().getOrderStatus() == OrderStatus.CREATED) {
                orderRepository.deleteById(id);
            }
        }
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    private List<ViewOrderDetailDTO> mapOrderDetailDTO(Order order) {
        List<ViewOrderDetailDTO> viewOrderDetailList = new ArrayList<>();

        List<OrderDetail> orderDetails = order.getOrderDetails();
        for (OrderDetail orderDetail : orderDetails) {
            ViewOrderDetailDTO viewOrderDetailDTO = new ViewOrderDetailDTO();
            viewOrderDetailDTO.setBrand(orderDetail.getProduct().getBrand());
            viewOrderDetailDTO.setName(orderDetail.getProduct().getName());
            viewOrderDetailDTO.setQuantity(orderDetail.getQuantity());
            viewOrderDetailDTO.setPackaging(orderDetail.getProduct().getPackaging());
            viewOrderDetailDTO.setGender(orderDetail.getProduct().getGender().getGender());
            if (orderDetail.getProduct().getImage() != null) {
                viewOrderDetailDTO.setImage(Base64.getEncoder().encodeToString(orderDetail.getProduct().getImage().getImage()));
            }
            viewOrderDetailList.add(viewOrderDetailDTO);
        }
        return viewOrderDetailList;
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
