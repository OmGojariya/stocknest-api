package com.stocknest.stocknest_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        String uri = request.getRequestURI();

        // Allow public endpoints - check both servletPath and requestURI
        if (path.startsWith("/api/auth/") || uri.startsWith("/api/auth/") || 
            path.equals("/api/auth") || uri.equals("/api/auth") ||
            path.startsWith("/actuator/") || uri.startsWith("/actuator/") ||
            path.startsWith("/error") || uri.startsWith("/error")) {
            System.out.println("JwtFilter: Bypassing authentication for: " + uri);
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("JwtFilter: Processing authentication for: " + uri);
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        try {
            // Extract token
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                username = jwtUtil.extractUsername(token);
            } else {
                System.out.println("No valid Authorization header found for: " + uri);
                // For protected endpoints without valid auth header, let Spring Security handle it
                filterChain.doFilter(request, response);
                return;
            }

            // Authenticate if valid token and no existing authentication
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println("JWT Authentication successful for user: " + username);
                } else {
                    System.out.println("JWT Token validation failed for user: " + username);
                }
            }
        } catch (UsernameNotFoundException e) {
            System.err.println("User not found: " + username);
        } catch (Exception e) {
            System.err.println("JWT Authentication failed: " + e.getMessage());
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}
