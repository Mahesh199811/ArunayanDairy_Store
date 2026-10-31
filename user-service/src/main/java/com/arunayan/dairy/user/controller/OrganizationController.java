package com.arunayan.dairy.user.controller;

import com.arunayan.dairy.user.domain.UserAccount;
import com.arunayan.dairy.user.dto.AddOrganizationMemberRequest;
import com.arunayan.dairy.user.dto.CreateOrganizationRequest;
import com.arunayan.dairy.user.dto.OrganizationResponse;
import com.arunayan.dairy.user.service.OrganizationService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public List<OrganizationResponse> myOrganizations(@AuthenticationPrincipal UserAccount user) {
        return organizationService.findForMember(user);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public OrganizationResponse create(
            @Valid @RequestBody CreateOrganizationRequest request,
            @AuthenticationPrincipal UserAccount user) {
        return organizationService.create(request, user);
    }

    @PostMapping("/{organizationId}/members")
    @PreAuthorize("hasRole('ADMIN')")
    public OrganizationResponse addMember(
            @PathVariable String organizationId,
            @Valid @RequestBody AddOrganizationMemberRequest request) {
        return organizationService.addMember(organizationId, request);
    }
}
