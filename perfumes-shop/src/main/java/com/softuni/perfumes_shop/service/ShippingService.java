package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.dto.outbound.ViewShippingDetailDTO;

import java.util.List;

public interface ShippingService {

    void createShipping(Long id);

    List<ViewShippingDetailDTO> getAllShipments();
}
