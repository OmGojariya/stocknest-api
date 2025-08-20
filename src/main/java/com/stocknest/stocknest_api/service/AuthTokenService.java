package com.stocknest.stocknest_api.service;

import com.stocknest.stocknest_api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final JwtUtil jwtUtil;

    /**
     * Extract JWT token from request header
     */
    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Validate token and get user ID
     */
    public String validateTokenAndGetUserId(String token) {
        if (token == null) {
            return null;
        }

        try {
            String username = jwtUtil.extractUsername(token);
            if (jwtUtil.validateToken(token, username)) {
                return username;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get authenticated user ID from request
     */
    public String getAuthenticatedUserId(HttpServletRequest request) {
        String token = extractToken(request);
        return validateTokenAndGetUserId(token);
    }
}
