package com.arunayan.dairy.user.service;

import com.arunayan.dairy.user.domain.Role;
import com.arunayan.dairy.user.domain.UserAccount;
import com.arunayan.dairy.user.dto.AuthResponse;
import com.arunayan.dairy.user.dto.LoginRequest;
import com.arunayan.dairy.user.dto.RegisterRequest;
import com.arunayan.dairy.user.dto.UserResponse;
import com.arunayan.dairy.user.repository.UserRepository;
import com.arunayan.dairy.user.security.JwtService;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Set;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Password must be no more than 72 UTF-8 bytes.");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists.");
        }

        UserAccount user = new UserAccount(
                request.name().trim(),
                email,
                passwordEncoder.encode(request.password()),
                Set.of(Role.USER));
        try {
            return createAuthResponse(userRepository.save(user));
        } catch (DuplicateKeyException exception) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "An account with this email already exists.", exception);
        }
    }

    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.password()));
        return createAuthResponse((UserAccount) authentication.getPrincipal());
    }

    public UserResponse getCurrentUser(UserAccount user) {
        return toUserResponse(user);
    }

    private AuthResponse createAuthResponse(UserAccount user) {
        return new AuthResponse(jwtService.createToken(user), "Bearer", jwtService.getExpirationSeconds(),
                toUserResponse(user));
    }

    private UserResponse toUserResponse(UserAccount user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRoles());
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
