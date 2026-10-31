package com.arunayan.dairy.product.domain;

import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("products")
public class Product {
    @Id
    private String id;
    @Indexed(unique = true)
    private String sku;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private String unit;
    private String imageUrl;
    private int stock;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    protected Product() {}

    public Product(String sku, String name, String description, String category,
                   BigDecimal price, String unit, String imageUrl, int stock) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.unit = unit;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.active = true;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void update(String name, String description, String category, BigDecimal price, String unit, String imageUrl) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.unit = unit;
        this.imageUrl = imageUrl;
        this.updatedAt = Instant.now();
    }

    public void setActive(boolean active) {
        this.active = active;
        this.updatedAt = Instant.now();
    }

    public void adjustStock(int stock) {
        this.stock = stock;
        this.updatedAt = Instant.now();
    }

    public String getId() { return id; }
    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public BigDecimal getPrice() { return price; }
    public String getUnit() { return unit; }
    public String getImageUrl() { return imageUrl; }
    public int getStock() { return stock; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
