package com.arunayan.dairy.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrganizationRequest(@NotBlank @Size(max = 120) String name) {
}
