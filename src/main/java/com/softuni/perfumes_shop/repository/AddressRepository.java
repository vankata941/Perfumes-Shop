package com.softuni.perfumes_shop.repository;

import com.softuni.perfumes_shop.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByCountryIgnoreCaseAndCityIgnoreCaseAndShippingAddressIgnoreCaseAndPostalCode(String country, String city, String shippingAddress, String postalCode);
}
