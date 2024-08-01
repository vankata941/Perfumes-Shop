package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.model.entity.PaymentDetail;
import com.softuni.perfumes_shop.repository.PaymentDetailRepository;
import com.softuni.perfumes_shop.service.PaymentDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentDetailServiceImpl implements PaymentDetailService {

    private final PaymentDetailRepository paymentDetailRepository;

    @Override
    public void savePaymentDetail(PaymentDetail paymentDetail) {
        paymentDetailRepository.save(paymentDetail);
    }

    @Override
    public Optional<PaymentDetail> findPaymentDetailIfExists(String cardHolder, String cardNumber, String cardExpiration, String securityCode) {
        return paymentDetailRepository.findByCardHolderIgnoreCaseAndCardNumberAndCardExpirationAndSecurityCode(cardHolder, cardNumber, cardExpiration, securityCode);
    }
}
