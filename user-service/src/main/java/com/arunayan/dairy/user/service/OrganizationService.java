package com.arunayan.dairy.user.service;

import com.arunayan.dairy.user.domain.Organization;
import com.arunayan.dairy.user.domain.UserAccount;
import com.arunayan.dairy.user.dto.AddOrganizationMemberRequest;
import com.arunayan.dairy.user.dto.CreateOrganizationRequest;
import com.arunayan.dairy.user.dto.OrganizationResponse;
import com.arunayan.dairy.user.repository.OrganizationRepository;
import com.arunayan.dairy.user.repository.UserRepository;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    public OrganizationService(OrganizationRepository organizationRepository, UserRepository userRepository) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
    }

    public OrganizationResponse create(CreateOrganizationRequest request, UserAccount owner) {
        Organization organization = organizationRepository.save(
                new Organization(request.name().trim(), owner.getId()));
        return toResponse(organization);
    }

    public List<OrganizationResponse> findForMember(UserAccount user) {
        return organizationRepository.findAllByMemberUserIdsContaining(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public OrganizationResponse addMember(
            String organizationId, AddOrganizationMemberRequest request) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found."));
        UserAccount member = userRepository.findByEmailIgnoreCase(request.email().trim().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        organization.addMember(member.getId());
        return toResponse(organizationRepository.save(organization));
    }

    private OrganizationResponse toResponse(Organization organization) {
        return new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                organization.getOwnerUserId(),
                organization.getMemberUserIds().size(),
                organization.getCreatedAt());
    }
}
