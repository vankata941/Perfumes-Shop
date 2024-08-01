package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.model.entity.Address;
import com.softuni.perfumes_shop.repository.AddressRepository;
import com.softuni.perfumes_shop.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public void saveAddress(Address address) {
        addressRepository.save(address);
    }

    @Override
    public Optional<Address> findAddressIfExists(String country, String city, String shippingAddress, String postalCode) {
        return addressRepository.findByCountryIgnoreCaseAndCityIgnoreCaseAndShippingAddressIgnoreCaseAndPostalCode(country, city, shippingAddress, postalCode);
    }
}
