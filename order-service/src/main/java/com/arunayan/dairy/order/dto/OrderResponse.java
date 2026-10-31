package com.arunayan.dairy.order.dto;

import com.arunayan.dairy.order.domain.CustomerOrder;
import com.arunayan.dairy.order.domain.OrderItem;
import com.arunayan.dairy.order.domain.OrderStatus;
import com.arunayan.dairy.order.domain.PaymentStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        String id, String orderNumber, List<OrderItem> items, BigDecimal total,
        String deliveryAddress, OrderStatus status, PaymentStatus paymentStatus, Instant createdAt) {
    public static OrderResponse from(CustomerOrder order) {
        return new OrderResponse(order.getId(), order.getOrderNumber(), order.getItems(), order.getTotal(),
                order.getDeliveryAddress(), order.getStatus(), order.getPaymentStatus(), order.getCreatedAt());
    }
}
