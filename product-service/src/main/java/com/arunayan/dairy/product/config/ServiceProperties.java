package com.arunayan.dairy.product.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record ServiceProperties(Jwt jwt, Cors cors, Internal internal) {
    public record Jwt(String secretBase64) {}
    public record Cors(String allowedOrigin) {}
    public record Internal(String serviceKey) {}
}
