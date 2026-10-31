package com.arunayan.dairy.product.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record StockAdjustmentRequest(@NotNull @PositiveOrZero Integer stock) {}
