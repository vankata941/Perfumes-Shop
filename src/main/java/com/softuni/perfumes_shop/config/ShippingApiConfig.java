package com.softuni.perfumes_shop.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "shipping.api")
public class ShippingApiConfig {

    private String key;

    private String baseUrl;


}
