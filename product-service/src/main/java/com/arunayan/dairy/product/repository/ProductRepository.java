package com.arunayan.dairy.product.repository;

import com.arunayan.dairy.product.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findAllByActiveTrueOrderByNameAsc();
    Optional<Product> findByIdAndActiveTrue(String id);
}
