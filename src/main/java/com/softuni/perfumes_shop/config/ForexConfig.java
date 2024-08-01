package com.softuni.perfumes_shop.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "forex.api")
public class ForexConfig {

    private String key;
    private String url;
    private String base;

    @PostConstruct
    public void checkConfig() {

        verifyValueNotNullOrBlank("key", key);
        verifyValueNotNullOrBlank("url", url);
        verifyValueNotNullOrBlank("base", base);

        if (!"USD".equals(base)) {
            throw new IllegalStateException("Sorry, but our plan does not support base different than USD!");
        }
    }

    private static void verifyValueNotNullOrBlank(String name, String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(String.format("%s is required!", name));
        }
    }
}
