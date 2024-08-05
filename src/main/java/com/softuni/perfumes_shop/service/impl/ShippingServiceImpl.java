package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.config.ShippingApiConfig;
import com.softuni.perfumes_shop.model.dto.inbound.StatusDTO;
import com.softuni.perfumes_shop.model.dto.inbound.ViewShippingDetailDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ShippingAddressDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ShippingDetailDTO;
import com.softuni.perfumes_shop.model.entity.Order;
import com.softuni.perfumes_shop.model.enums.OrderStatus;
import com.softuni.perfumes_shop.repository.OrderRepository;
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
    private final RestClient genericRestClient;
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final ShippingApiConfig shippingApiConfig;

    public ShippingServiceImpl(@Qualifier(value = "shippingRestClient") RestClient restClient,
                               OrderService orderService,
                               ModelMapper modelMapper, OrderRepository orderRepository,
                               @Qualifier(value = "genericRestClient") RestClient genericRestClient, ShippingApiConfig shippingApiConfig)
    {
        this.restClient = restClient;
        this.orderService = orderService;
        this.modelMapper = modelMapper;
        this.genericRestClient = genericRestClient;
        this.shippingApiConfig = shippingApiConfig;
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
    public List<ViewShippingDetailDTO> getAllShipping() {
        updateStatuses();
        return restClient
                .get()
                .uri("/shipping/all")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }

    @Override
    public void deleteShippingByOrderId(Long id) {
        Optional<Order> optOrder = orderService.getOrderById(id);
        if (optOrder.isPresent() && optOrder.get().getOrderStatus() == OrderStatus.DELIVERED) {
            restClient
                    .delete()
                    .uri("/shipping/delete/{id}", id)
                    .retrieve();
        }
    }

    private List<StatusDTO> getAllStatuses() {

        return genericRestClient
                .get()
                .uri(shippingApiConfig.getBaseUrl() + "/shipping/status")
                .header("SHIPPING_KEY", shippingApiConfig.getKey())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }

    @Override
    public void updateStatuses() {
        List<StatusDTO> allStatuses = getAllStatuses();
        allStatuses.forEach(orderStatus -> {
            Optional<Order> optOrder = orderService.getOrderById(orderStatus.getOrderId());
            if (optOrder.isPresent()) {
                Order order = optOrder.get();
                order.setOrderStatus(orderStatus.getStatus());
                orderService.saveOrder(order);
            }
        });
    }
}
