package com.arunayan.dairy.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record ServiceProperties(Jwt jwt, Cors cors, Products products, Internal internal) {
    public record Jwt(String secretBase64) {}
    public record Cors(String allowedOrigin) {}
    public record Products(String baseUrl) {}
    public record Internal(String serviceKey) {}
}
