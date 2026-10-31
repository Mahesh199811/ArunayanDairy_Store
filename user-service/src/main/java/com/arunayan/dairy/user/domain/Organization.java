package com.arunayan.dairy.user.domain;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("organizations")
public class Organization {

    @Id
    private String id;

    private String name;

    private String ownerUserId;

    @Indexed
    private Set<String> memberUserIds = new HashSet<>();

    private Instant createdAt;

    protected Organization() {
    }

    public Organization(String name, String ownerUserId) {
        this.name = name;
        this.ownerUserId = ownerUserId;
        this.memberUserIds.add(ownerUserId);
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public Set<String> getMemberUserIds() {
        return Set.copyOf(memberUserIds);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void addMember(String userId) {
        memberUserIds.add(userId);
    }
}
