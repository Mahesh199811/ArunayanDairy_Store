package com.arunayan.dairy.order.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("orders")
public class CustomerOrder {
    @Id
    private String id;
    @Indexed
    private String userId;
    private String orderNumber;
    private List<OrderItem> items;
    private BigDecimal total;
    private String deliveryAddress;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private Instant createdAt;

    protected CustomerOrder() {}

    public CustomerOrder(String userId, List<OrderItem> items, BigDecimal total, String deliveryAddress) {
        this.userId = userId;
        this.orderNumber = "ARD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.items = List.copyOf(items);
        this.total = total;
        this.deliveryAddress = deliveryAddress;
        this.status = OrderStatus.CONFIRMED;
        this.paymentStatus = PaymentStatus.SIMULATED_PAID;
        this.createdAt = Instant.now();
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getOrderNumber() { return orderNumber; }
    public List<OrderItem> getItems() { return List.copyOf(items); }
    public BigDecimal getTotal() { return total; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public OrderStatus getStatus() { return status; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public Instant getCreatedAt() { return createdAt; }
}
