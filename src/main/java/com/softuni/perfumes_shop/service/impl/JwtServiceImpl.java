package com.softuni.perfumes_shop.service.impl;

import com.softuni.perfumes_shop.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    private final String jwtSecret;

    private final long jwtExpire;

    public JwtServiceImpl(@Value("${jwt.secret}") String jwtSecret, @Value("${jwt.expiration}") long jwtExpire) {
        this.jwtSecret = jwtSecret;
        this.jwtExpire = jwtExpire;
    }

    @Override
    public String generateToken(String userId, Map<String, Object> claims) {
        Date now = new Date();
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(new Date(now.getTime() + jwtExpire))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
