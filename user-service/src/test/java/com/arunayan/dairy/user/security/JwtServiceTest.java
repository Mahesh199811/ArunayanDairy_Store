package com.arunayan.dairy.user.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.arunayan.dairy.user.config.JwtProperties;
import com.arunayan.dairy.user.domain.Role;
import com.arunayan.dairy.user.domain.UserAccount;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private JwtService jwtService;
    private UserAccount user;

    @BeforeEach
    void setUp() {
        byte[] secret = new byte[32];
        java.util.Arrays.fill(secret, (byte) 7);
        JwtProperties properties = new JwtProperties(Base64.getEncoder().encodeToString(secret), Duration.ofMinutes(15));
        jwtService = new JwtService(Keys.hmacShaKeyFor(secret), properties);
        user = new UserAccount("Dairy Admin", "admin@example.com", "hashed-password", Set.of(Role.ADMIN));
    }

    @Test
    void createsShortLivedTokenWithUserIdentityAndRoles() {
        String token = jwtService.createToken(user);

        var claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtServiceSecret())))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals("admin@example.com", claims.getSubject());
        assertEquals(List.of("ADMIN"), claims.get("roles", List.class));
        assertEquals(900, jwtService.getExpirationSeconds());
    }

    @Test
    void rejectsTokenSignedWithAnotherKey() {
        String token = Jwts.builder()
                .subject("admin@example.com")
                .signWith(Keys.hmacShaKeyFor(new byte[32]))
                .compact();

        assertThrows(io.jsonwebtoken.JwtException.class, () -> jwtService.extractSubject(token));
    }

    private String jwtServiceSecret() {
        byte[] secret = new byte[32];
        java.util.Arrays.fill(secret, (byte) 7);
        return Base64.getEncoder().encodeToString(secret);
    }
}
