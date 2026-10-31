package com.arunayan.dairy.order.client;

import com.arunayan.dairy.order.config.ServiceProperties;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ProductClient {
    private final RestClient restClient;
    private final String serviceKey;

    public ProductClient(RestClient.Builder builder, ServiceProperties properties) {
        this.restClient = builder.baseUrl(properties.products().baseUrl()).build();
        this.serviceKey = properties.internal().serviceKey();
        if (serviceKey == null || serviceKey.isBlank()) {
            throw new IllegalStateException("INTERNAL_SERVICE_KEY must be configured.");
        }
    }

    public ProductSnapshot getProduct(String productId) {
        try {
            ProductSnapshot product = restClient.get()
                    .uri("/api/products/{id}", productId)
                    .retrieve()
                    .body(ProductSnapshot.class);
            if (product == null || !product.active()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Product is unavailable.");
            }
            return product;
        } catch (ResponseStatusException exception) {
            throw exception;
        } catch (RestClientResponseException exception) {
            HttpStatus status = HttpStatus.resolve(exception.getStatusCode().value());
            if (status != null && status.is4xxClientError()) {
                throw new ResponseStatusException(status, "Product is unavailable.", exception);
            }
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE, "Product service is unavailable.", exception);
        } catch (RuntimeException exception) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE, "Product service is unavailable.", exception);
        }
    }

    public void reserve(String productId, int quantity) {
        callInventory(productId, quantity, "reserve");
    }

    public void release(String productId, int quantity) {
        callInventory(productId, quantity, "release");
    }

    private void callInventory(String productId, int quantity, String action) {
        try {
            restClient.post()
                    .uri("/api/internal/inventory/{id}/{action}?quantity={quantity}", productId, action, quantity)
                    .header("X-Internal-Service-Key", serviceKey)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException exception) {
            HttpStatus status = HttpStatus.resolve(exception.getStatusCode().value());
            if (status != null && status.is4xxClientError()) {
                throw new ResponseStatusException(status, "Could not " + action + " product inventory.", exception);
            }
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE, "Product service is unavailable.", exception);
        } catch (RuntimeException exception) {
            if (exception instanceof ResponseStatusException statusException) {
                throw statusException;
            }
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Product service is unavailable.", exception);
        }
    }

    public record ProductSnapshot(
            String id, String name, String unit, java.math.BigDecimal price, boolean active) {
    }
}
