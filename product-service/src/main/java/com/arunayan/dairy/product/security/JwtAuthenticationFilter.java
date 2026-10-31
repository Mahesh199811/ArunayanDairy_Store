package com.arunayan.dairy.product.security;

import com.arunayan.dairy.product.config.ServiceProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final SecretKey key;

    public JwtAuthenticationFilter(ServiceProperties properties) {
        try {
            byte[] bytes = Base64.getDecoder().decode(properties.jwt().secretBase64());
            if (bytes.length < 32) throw new IllegalStateException("JWT_SECRET_BASE64 must decode to at least 32 bytes.");
            key = new SecretKeySpec(bytes, "HmacSHA256");
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("JWT_SECRET_BASE64 must be valid Base64.", exception);
        }
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Claims claims = Jwts.parser().verifyWith(key).build()
                        .parseSignedClaims(header.substring(7)).getPayload();
                String id = claims.get("userId", String.class);
                String email = claims.getSubject();
                List<?> rawRoles = claims.get("roles", List.class);
                if (id == null || email == null || rawRoles == null
                        || rawRoles.stream().anyMatch(role -> !(role instanceof String))) {
                    throw new JwtException("Required claims are missing.");
                }
                Set<String> roles = rawRoles.stream().map(String.class::cast).collect(Collectors.toUnmodifiableSet());
                AuthenticatedUser principal = new AuthenticatedUser(id, email, roles);
                var authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .toList();
                var authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException | IllegalArgumentException exception) {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
