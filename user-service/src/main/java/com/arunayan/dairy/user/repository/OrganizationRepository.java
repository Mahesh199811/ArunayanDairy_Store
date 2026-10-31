package com.arunayan.dairy.user.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.arunayan.dairy.user.domain.Organization;

public interface OrganizationRepository extends MongoRepository<Organization, String> {
    List<Organization> findAllByMemberUserIdsContaining(String userId);
}
