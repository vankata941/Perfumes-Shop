package com.softuni.perfumes_shop.config;

import com.softuni.perfumes_shop.service.JwtService;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Configuration
public class RestConfig {

    @Bean(name = "genericRestClient")
    public RestClient restClient() {
        return RestClient.create();
    }

    @Bean(name = "shippingRestClient")
    public RestClient shippingRestClient(ShippingApiConfig shippingApiConfig, ClientHttpRequestInterceptor requestInterceptor) {
        return RestClient
                .builder()
                .baseUrl(shippingApiConfig.getBaseUrl())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .requestInterceptor(requestInterceptor)
                .build();
    }

    @Bean
    public ClientHttpRequestInterceptor requestInterceptor(JwtService jwtService, CurrentUserDetails currentUserDetails) {
        return (r, b, e) -> {
            currentUserDetails.optCurrentUser().ifPresent(user -> {
                String bearerToken = jwtService.generateToken(
                    user.getUuid().toString(),
                        Map.of(
                                "roles",
                                currentUserDetails
                                        .getAuthorities()
                                        .stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .toList())
                );

                r.getHeaders().setBearerAuth(bearerToken);
            });
            return e.execute(r, b);
        };
    }
}
