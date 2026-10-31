package com.arunayan.dairy.product.controller;

import com.arunayan.dairy.product.config.ServiceProperties;
import com.arunayan.dairy.product.service.ProductService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/internal/inventory")
public class InternalInventoryController {
    private final ProductService productService;
    private final String serviceKey;

    public InternalInventoryController(ProductService productService, ServiceProperties properties) {
        this.productService = productService;
        this.serviceKey = properties.internal().serviceKey();
        if (serviceKey == null || serviceKey.isBlank()) {
            throw new IllegalStateException("INTERNAL_SERVICE_KEY must be configured.");
        }
    }

    @PostMapping("/{productId}/reserve")
    public void reserve(
            @PathVariable String productId,
            @RequestParam int quantity,
            @RequestHeader(name = "X-Internal-Service-Key", required = false) String suppliedKey) {
        authenticate(suppliedKey);
        productService.reserveStock(productId, quantity);
    }

    @PostMapping("/{productId}/release")
    public void release(
            @PathVariable String productId,
            @RequestParam int quantity,
            @RequestHeader(name = "X-Internal-Service-Key", required = false) String suppliedKey) {
        authenticate(suppliedKey);
        productService.returnStock(productId, quantity);
    }

    private void authenticate(String suppliedKey) {
        if (suppliedKey == null || !MessageDigest.isEqual(
                serviceKey.getBytes(StandardCharsets.UTF_8), suppliedKey.getBytes(StandardCharsets.UTF_8))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid internal service credentials.");
        }
    }
}
