package com.arunayan.dairy.user.dto;

import java.util.Set;
import com.arunayan.dairy.user.domain.Role;

public record UserResponse(String id, String name, String email, Set<Role> roles) {
}
