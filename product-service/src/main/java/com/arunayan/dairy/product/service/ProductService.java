package com.arunayan.dairy.product.service;

import com.arunayan.dairy.product.domain.Product;
import com.arunayan.dairy.product.dto.ProductRequest;
import com.arunayan.dairy.product.dto.ProductResponse;
import com.arunayan.dairy.product.repository.ProductRepository;
import java.util.List;
import java.util.Locale;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final MongoTemplate mongoTemplate;

    public ProductService(ProductRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    public List<ProductResponse> findAll() {
        return repository.findAllByActiveTrueOrderByNameAsc().stream().map(ProductResponse::from).toList();
    }

    public ProductResponse findById(String id) {
        return repository.findByIdAndActiveTrue(id)
                .map(ProductResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));
    }

    public ProductResponse create(ProductRequest request) {
        Product product = new Product(request.sku().trim().toUpperCase(Locale.ROOT),
                request.name().trim(), clean(request.description()), request.category().trim(),
                request.price(), request.unit().trim(), clean(request.imageUrl()), request.stock());
        return ProductResponse.from(repository.save(product));
    }

    public ProductResponse update(String id, ProductRequest request) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));
        if (!product.getSku().equalsIgnoreCase(request.sku().trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product SKU cannot be changed.");
        }
        product.update(request.name().trim(), clean(request.description()), request.category().trim(),
                request.price(), request.unit().trim(), clean(request.imageUrl()));
        return ProductResponse.from(repository.save(product));
    }

    public ProductResponse adjustStock(String id, int stock) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));
        return adjustStockValue(product, stock);
    }

    private ProductResponse adjustStockValue(Product product, int stock) {
        product.adjustStock(stock);
        return ProductResponse.from(repository.save(product));
    }

    public void reserveStock(String id, int quantity) {
        if (quantity <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be positive.");
        Query query = Query.query(Criteria.where("_id").is(id)
                .and("active").is(true).and("stock").gte(quantity));
        Update update = new Update().inc("stock", -quantity).set("updatedAt", java.time.Instant.now());
        if (mongoTemplate.updateFirst(query, update, Product.class).getModifiedCount() == 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product is unavailable or has insufficient stock.");
        }
    }

    public void returnStock(String id, int quantity) {
        if (quantity <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be positive.");
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update().inc("stock", quantity).set("updatedAt", java.time.Instant.now());
        if (mongoTemplate.updateFirst(query, update, Product.class).getMatchedCount() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
        }
    }

    public void deactivate(String id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));
        product.setActive(false);
        repository.save(product);
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
