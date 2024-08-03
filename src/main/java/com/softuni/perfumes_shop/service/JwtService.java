package com.softuni.perfumes_shop.service;

import java.util.Map;

public interface JwtService {

    String generateToken(String userId, Map<String, Object> claims);
}
