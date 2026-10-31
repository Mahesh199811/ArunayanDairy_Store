package com.arunayan.dairy.order.repository;

import com.arunayan.dairy.order.domain.CustomerOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<CustomerOrder, String> {
    List<CustomerOrder> findAllByUserIdOrderByCreatedAtDesc(String userId);
    Optional<CustomerOrder> findByIdAndUserId(String id, String userId);
}
