package com.arunayan.dairy.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank @Size(max = 100) String sku,
        @NotBlank @Size(max = 150) String name,
        @Size(max = 2000) String description,
        @NotBlank @Size(max = 80) String category,
        @NotNull @DecimalMin(value = "0.01") @Digits(integer = 8, fraction = 2) BigDecimal price,
        @NotBlank @Size(max = 40) String unit,
        @Size(max = 1000) String imageUrl,
        @NotNull @PositiveOrZero Integer stock) {}
