package com.softuni.perfumes_shop.scheduler;

import com.softuni.perfumes_shop.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderStatusUpdateScheduler {

    private final ShippingService shippingService;

    @Scheduled(cron = "0 * * * * ?", zone = "Europe/Sofia")
    private void updateOrderStatusScheduler() {
        shippingService.updateStatuses();
    }
}
