package com.arunayan.dairy.order.service;

import com.arunayan.dairy.order.client.ProductClient;
import com.arunayan.dairy.order.client.ProductClient.ProductSnapshot;
import com.arunayan.dairy.order.domain.CustomerOrder;
import com.arunayan.dairy.order.domain.OrderItem;
import com.arunayan.dairy.order.dto.CreateOrderRequest;
import com.arunayan.dairy.order.dto.OrderResponse;
import com.arunayan.dairy.order.repository.OrderRepository;
import com.arunayan.dairy.order.security.AuthenticatedUser;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository repository;
    private final ProductClient productClient;

    public OrderService(OrderRepository repository, ProductClient productClient) {
        this.repository = repository;
        this.productClient = productClient;
    }

    public OrderResponse create(CreateOrderRequest request, AuthenticatedUser user) {
        Map<String, Integer> quantities = new LinkedHashMap<>();
        request.items().forEach(item -> quantities.merge(item.productId(), item.quantity(), Math::addExact));
        if (quantities.values().stream().anyMatch(quantity -> quantity > 100)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Combined quantity per product cannot exceed 100.");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            ProductSnapshot product = productClient.getProduct(entry.getKey());
            BigDecimal lineTotal = product.price().multiply(BigDecimal.valueOf(entry.getValue()));
            total = total.add(lineTotal);
            orderItems.add(new OrderItem(product.id(), product.name(), product.unit(),
                    product.price(), entry.getValue(), lineTotal));
        }

        List<OrderItem> reserved = new ArrayList<>();
        try {
            for (OrderItem item : orderItems) {
                productClient.reserve(item.productId(), item.quantity());
                reserved.add(item);
            }
            CustomerOrder order = new CustomerOrder(
                    user.id(), orderItems, total, request.deliveryAddress().trim());
            return OrderResponse.from(repository.save(order));
        } catch (RuntimeException exception) {
            releaseReservations(reserved);
            throw exception;
        }
    }

    public List<OrderResponse> findForUser(AuthenticatedUser user) {
        if (user.isAdmin()) {
            return repository.findAll().stream().map(OrderResponse::from).toList();
        }
        return repository.findAllByUserIdOrderByCreatedAtDesc(user.id())
                .stream().map(OrderResponse::from).toList();
    }

    public OrderResponse findById(String id, AuthenticatedUser user) {
        CustomerOrder order = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found."));
        if (!user.isAdmin() && !order.getUserId().equals(user.id())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found.");
        }
        return OrderResponse.from(order);
    }

    private void releaseReservations(List<OrderItem> reserved) {
        for (int index = reserved.size() - 1; index >= 0; index--) {
            OrderItem item = reserved.get(index);
            try {
                productClient.release(item.productId(), item.quantity());
            } catch (RuntimeException releaseFailure) {
                logger.error("Failed to release stock reservation for product {} after order failure.",
                        item.productId(), releaseFailure);
            }
        }
    }
}
