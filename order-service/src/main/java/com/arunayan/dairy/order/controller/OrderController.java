package com.arunayan.dairy.order.controller;

import com.arunayan.dairy.order.dto.CreateOrderRequest;
import com.arunayan.dairy.order.dto.OrderResponse;
import com.arunayan.dairy.order.security.AuthenticatedUser;
import com.arunayan.dairy.order.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(
            @Valid @RequestBody CreateOrderRequest request,
            @AuthenticationPrincipal AuthenticatedUser user) {
        return service.create(request, user);
    }

    @GetMapping
    public List<OrderResponse> findMine(@AuthenticationPrincipal AuthenticatedUser user) {
        return service.findForUser(user);
    }

    @GetMapping("/{id}")
    public OrderResponse findById(
            @PathVariable String id, @AuthenticationPrincipal AuthenticatedUser user) {
        return service.findById(id, user);
    }

}
