package com.softuni.perfumes_shop.repository;

import com.softuni.perfumes_shop.model.entity.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
    Optional<PaymentDetail> findByCardHolderIgnoreCaseAndCardNumberAndCardExpirationAndSecurityCode(String cardHolder, String cardNumber, String cardExpiration, String securityCode);

}
