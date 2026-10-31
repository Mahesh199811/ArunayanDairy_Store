package com.arunayan.dairy.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.arunayan.dairy.order.client.ProductClient;
import com.arunayan.dairy.order.client.ProductClient.ProductSnapshot;
import com.arunayan.dairy.order.domain.CustomerOrder;
import com.arunayan.dairy.order.dto.CreateOrderItemRequest;
import com.arunayan.dairy.order.dto.CreateOrderRequest;
import com.arunayan.dairy.order.repository.OrderRepository;
import com.arunayan.dairy.order.security.AuthenticatedUser;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class OrderServiceTest {
    private final OrderRepository repository = org.mockito.Mockito.mock(OrderRepository.class);
    private final ProductClient productClient = org.mockito.Mockito.mock(ProductClient.class);
    private final OrderService service = new OrderService(repository, productClient);
    private final AuthenticatedUser user = new AuthenticatedUser("user-1", "customer@example.com", Set.of("USER"));

    @Test
    void usesCatalogPriceAndReservesStockWhenCreatingOrder() {
        when(productClient.getProduct("product-1"))
                .thenReturn(new ProductSnapshot("product-1", "Fresh Milk", "1 litre", new BigDecimal("68.00"), true));
        when(repository.save(org.mockito.ArgumentMatchers.any(CustomerOrder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        var request = new CreateOrderRequest(
                List.of(new CreateOrderItemRequest("product-1", 2)), "12 Farm Lane");

        var response = service.create(request, user);

        assertEquals(new BigDecimal("136.00"), response.total());
        assertEquals("Fresh Milk", response.items().get(0).name());
        assertEquals("SIMULATED_PAID", response.paymentStatus().name());
        verify(productClient).reserve("product-1", 2);
    }

    @Test
    void returnsAlreadyReservedStockWhenLaterReservationFails() {
        when(productClient.getProduct("product-1"))
                .thenReturn(new ProductSnapshot("product-1", "Milk", "1 litre", new BigDecimal("68.00"), true));
        when(productClient.getProduct("product-2"))
                .thenReturn(new ProductSnapshot("product-2", "Curd", "500 g", new BigDecimal("75.00"), true));
        org.mockito.Mockito.doNothing().when(productClient).reserve("product-1", 1);
        org.mockito.Mockito.doThrow(new ResponseStatusException(HttpStatus.CONFLICT, "No stock"))
                .when(productClient).reserve("product-2", 1);
        var request = new CreateOrderRequest(List.of(
                new CreateOrderItemRequest("product-1", 1),
                new CreateOrderItemRequest("product-2", 1)), "12 Farm Lane");

        assertThrows(ResponseStatusException.class, () -> service.create(request, user));

        verify(productClient).release("product-1", 1);
        verify(repository, never()).save(org.mockito.ArgumentMatchers.any(CustomerOrder.class));
    }
}
