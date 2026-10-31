package com.arunayan.dairy.product.dto;

import com.arunayan.dairy.product.domain.Product;
import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        String id, String sku, String name, String description, String category,
        BigDecimal price, String unit, String imageUrl, int stock, boolean active,
        Instant createdAt, Instant updatedAt) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getSku(), product.getName(),
                product.getDescription(), product.getCategory(), product.getPrice(),
                product.getUnit(), product.getImageUrl(), product.getStock(), product.isActive(),
                product.getCreatedAt(), product.getUpdatedAt());
    }
}
