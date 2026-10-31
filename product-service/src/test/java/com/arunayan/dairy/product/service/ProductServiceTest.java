package com.arunayan.dairy.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.arunayan.dairy.product.domain.Product;
import com.arunayan.dairy.product.repository.ProductRepository;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class ProductServiceTest {
    private final ProductRepository repository = org.mockito.Mockito.mock(ProductRepository.class);
    private final MongoTemplate mongoTemplate = org.mockito.Mockito.mock(MongoTemplate.class);
    private final ProductService service = new ProductService(repository, mongoTemplate);

    @Test
    void rejectsReservationWhenAvailableStockIsInsufficient() {
        when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(Product.class)))
                .thenReturn(UpdateResult.acknowledged(0L, 0L, null));

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> service.reserveStock("product-1", 2));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(mongoTemplate).updateFirst(any(Query.class), any(Update.class), eq(Product.class));
    }

    @Test
    void rejectsNonPositiveStockReservationBeforeDatabaseWrite() {
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> service.reserveStock("product-1", 0));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        org.mockito.Mockito.verifyNoInteractions(mongoTemplate);
    }
}
