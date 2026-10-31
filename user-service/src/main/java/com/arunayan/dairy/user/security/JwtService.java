package com.arunayan.dairy.user.security;

import com.arunayan.dairy.user.config.JwtProperties;
import com.arunayan.dairy.user.domain.UserAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final JwtProperties properties;

    public JwtService(SecretKey signingKey, JwtProperties properties) {
        if (properties.expiration() == null || properties.expiration().isZero()
                || properties.expiration().isNegative()) {
            throw new IllegalStateException("JWT_EXPIRATION must be a positive duration.");
        }
        this.signingKey = signingKey;
        this.properties = properties;
    }

    public String createToken(UserAccount user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("roles", user.getRoles().stream().map(Enum::name).toList())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(properties.expiration())))
                .signWith(signingKey)
                .compact();
    }

    public String extractSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isValid(String token) {
        parseClaims(token);
        return true;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long getExpirationSeconds() {
        return properties.expiration().toSeconds();
    }
}
