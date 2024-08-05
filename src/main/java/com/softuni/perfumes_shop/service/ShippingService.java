package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.dto.inbound.ViewShippingDetailDTO;

import java.util.List;

public interface ShippingService {

    void createShipping(Long id);

    List<ViewShippingDetailDTO> getAllShipping();

    void deleteShippingByOrderId(Long id);

    void updateStatuses();
}
