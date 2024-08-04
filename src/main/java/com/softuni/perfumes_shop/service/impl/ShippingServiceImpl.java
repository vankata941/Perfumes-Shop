package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.model.dto.inbound.ViewShippingDetailDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ShippingAddressDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ShippingDetailDTO;
import com.softuni.perfumes_shop.model.entity.Order;
import com.softuni.perfumes_shop.service.OrderService;
import com.softuni.perfumes_shop.service.ShippingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
public class ShippingServiceImpl implements ShippingService {

    private final RestClient restClient;
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    public ShippingServiceImpl(@Qualifier(value = "shippingRestClient") RestClient restClient,
                               OrderService orderService,
                               ModelMapper modelMapper)
    {
        this.restClient = restClient;
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }


    private void createShippingDetail(ShippingDetailDTO shippingDetailDTO) {

        restClient
                .post()
                .uri("/shipping/create")
                .body(shippingDetailDTO)
                .retrieve();
    }

    @Override
    public void createShipping(Long id) {
        Optional<Order> optOrder = orderService.getOrderById(id);

        if (optOrder.isPresent()) {
            Order order = optOrder.get();
            ShippingDetailDTO shippingDetailDTO = new ShippingDetailDTO();
            shippingDetailDTO.setOrderId(order.getId());
            shippingDetailDTO.setFirstName(order.getUserDetail().getFirstName());
            shippingDetailDTO.setLastName(order.getUserDetail().getLastName());
            shippingDetailDTO.setPhoneNumber(order.getUserDetail().getPhoneNumber());
            shippingDetailDTO.setStatus(order.getOrderStatus());
            shippingDetailDTO.setOrderCreatedDate(order.getOrderCreatedDate());

            ShippingAddressDTO shippingAddressDTO = modelMapper.map(order.getAddress(), ShippingAddressDTO.class);
            shippingDetailDTO.setShippingAddress(shippingAddressDTO);

            createShippingDetail(shippingDetailDTO);
        }
    }

    @Override
    public List<ViewShippingDetailDTO> getAllShipments() {

        return restClient
                .get()
                .uri("/shipping/all")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }


}
