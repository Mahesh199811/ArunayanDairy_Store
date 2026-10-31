package com.arunayan.dairy.order.security;

import java.util.Set;

public record AuthenticatedUser(String id, String email, Set<String> roles) {
    public boolean isAdmin() {
        return roles.contains("ADMIN");
    }
}
