package com.arunayan.dairy.order.domain;

import java.math.BigDecimal;

public record OrderItem(
        String productId, String name, String unit, BigDecimal unitPrice, int quantity, BigDecimal lineTotal) {
}
