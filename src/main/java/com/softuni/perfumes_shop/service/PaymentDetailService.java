package com.softuni.perfumes_shop.service;

import com.softuni.perfumes_shop.model.entity.PaymentDetail;

import java.util.Optional;

public interface PaymentDetailService {
    void savePaymentDetail(PaymentDetail paymentDetail);

    Optional<PaymentDetail> findPaymentDetailIfExists(String cardHolder, String cardNumber, String cardExpiration, String securityCode);
}
