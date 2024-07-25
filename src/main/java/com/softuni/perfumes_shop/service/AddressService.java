package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.entity.Address;

import java.util.Optional;

public interface AddressService {
    void saveAddress(Address address);

    Optional<Address> findAddressIfExists(String country, String city, String shippingAddress, String postalCode);
}
