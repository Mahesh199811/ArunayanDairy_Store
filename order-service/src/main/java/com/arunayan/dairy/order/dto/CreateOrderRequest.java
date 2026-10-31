package com.arunayan.dairy.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateOrderRequest(
        @NotEmpty @Size(max = 30) List<@Valid CreateOrderItemRequest> items,
        @NotBlank @Size(max = 500) String deliveryAddress) {
}
