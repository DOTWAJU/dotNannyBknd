package com.dotnanny.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/** Reads a Bearer JWT, validates it, and populates the security context. */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwt;

    public JwtAuthFilter(JwtService jwt) {
        this.jwt = jwt;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try {
                Claims claims = jwt.parse(header.substring(7)).getPayload();
                String email = claims.getSubject();
                String role = claims.get("role", String.class);
                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + (role == null ? "USER" : role.toUpperCase())));
                var auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                // Invalid/expired token: leave the request unauthenticated.
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
