package com.arunayan.dairy.user.controller;

import com.arunayan.dairy.user.domain.UserAccount;
import com.arunayan.dairy.user.dto.AuthResponse;
import com.arunayan.dairy.user.dto.LoginRequest;
import com.arunayan.dairy.user.dto.RegisterRequest;
import com.arunayan.dairy.user.dto.UserResponse;
import com.arunayan.dairy.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserResponse currentUser(@AuthenticationPrincipal UserAccount user) {
        return authService.getCurrentUser(user);
    }
}
