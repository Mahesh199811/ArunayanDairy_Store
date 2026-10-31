package com.arunayan.dairy.user.dto;

import java.time.Instant;
public record OrganizationResponse(
        String id, String name, String ownerUserId, int memberCount, Instant createdAt) {
}
