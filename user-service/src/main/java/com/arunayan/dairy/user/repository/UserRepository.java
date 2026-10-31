package com.arunayan.dairy.user.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.arunayan.dairy.user.domain.UserAccount;

public interface UserRepository extends MongoRepository<UserAccount, String> {
    Optional<UserAccount> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}
