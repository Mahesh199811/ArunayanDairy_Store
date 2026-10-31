package com.arunayan.dairy.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public record CreateOrderItemRequest(@NotBlank String productId, @Positive @Max(100) int quantity) {
}
