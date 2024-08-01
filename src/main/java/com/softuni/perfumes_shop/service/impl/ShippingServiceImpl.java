package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.config.ShippingApiConfig;
import com.softuni.perfumes_shop.model.dto.inbound.ViewShippingDetailDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ShippingAddressDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ShippingDetailDTO;
import com.softuni.perfumes_shop.model.entity.Order;
import com.softuni.perfumes_shop.service.OrderService;
import com.softuni.perfumes_shop.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

    private final RestClient restClient;
    private final ShippingApiConfig shippingApiConfig;
    private final OrderService orderService;
    private final ModelMapper modelMapper;


    private void createShippingDetail(ShippingDetailDTO shippingDetailDTO) {

        restClient
                .post()
                .uri("http://localhost:8081/shipping/create")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
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
                .uri("http://localhost:8081/shipping/all")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }


}
