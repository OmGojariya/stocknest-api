package com.stocknest.stocknest_api.controller;

import com.stocknest.stocknest_api.service.AngelOneService;
import com.stocknest.stocknest_api.service.AuthTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/angelone")
@RequiredArgsConstructor
@Slf4j
public class AngelOneController {

    private final AngelOneService angelOneService;
    private final AuthTokenService authTokenService;

    /**
     * Initiate AngelOne OAuth login
     */
    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> initiateLogin(HttpServletRequest request) {
        try {
            String userId = authTokenService.getAuthenticatedUserId(request);
            if (userId == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid or missing token"));
            }

            String authUrl = angelOneService.getAuthorizationUrl(userId);

            return ResponseEntity.ok(Map.of(
                    "message", "Redirect to AngelOne for authorization",
                    "authUrl", authUrl,
                    "state", userId
            ));
        } catch (Exception e) {
            log.error("Error initiating AngelOne login: ", e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    /**
     * Handle AngelOne OAuth callback
     */
    @GetMapping("/callback")
    public Mono<ResponseEntity<Map<String, Object>>> handleCallback(
            @RequestParam("auth_code") String authCode,
            @RequestParam("state") String userId) {
        
        log.info("AngelOne callback received for user: {} with code: {}", userId, authCode);
        
        return angelOneService.exchangeCodeForToken(authCode, userId)
                .map(response -> ResponseEntity.ok(Map.of(
                        "message", "Successfully connected to AngelOne",
                        "status", "success",
                        "data", response
                )))
                .onErrorReturn(ResponseEntity.status(500).body(Map.of(
                        "message", "Failed to connect to AngelOne",
                        "status", "error"
                )));
    }

    /**
     * Handle AngelOne postback (webhooks)
     */
    @PostMapping("/postback")
    public ResponseEntity<Map<String, String>> handlePostback(@RequestBody Map<String, Object> postbackData) {
        log.info("AngelOne postback received: {}", postbackData);
        
        // Process postback data (order updates, etc.)
        // You can implement custom logic here based on your requirements
        
        return ResponseEntity.ok(Map.of("status", "acknowledged"));
    }

    /**
     * Get user profile from AngelOne
     */
    @GetMapping("/profile")
    public Mono<ResponseEntity<Map<String, Object>>> getUserProfile(HttpServletRequest request) {
        try {
            String userId = authTokenService.getAuthenticatedUserId(request);
            if (userId == null) {
                return Mono.just(ResponseEntity.status(401).body(Map.of("error", "Invalid or missing token")));
            }
            
            return angelOneService.getUserProfile(userId)
                    .map(ResponseEntity::ok)
                    .onErrorReturn(ResponseEntity.status(500).body(Map.of("error", "Failed to get profile")));
        } catch (Exception e) {
            log.error("Error getting AngelOne profile: ", e);
            return Mono.just(ResponseEntity.status(500).body(Map.of("error", "Internal server error")));
        }
    }

    /**
     * Get holdings from AngelOne
     */
    @GetMapping("/holdings")
    public Mono<ResponseEntity<Map<String, Object>>> getHoldings(HttpServletRequest request) {
        try {
            String userId = authTokenService.getAuthenticatedUserId(request);
            if (userId == null) {
                return Mono.just(ResponseEntity.status(401).body(Map.of("error", "Invalid or missing token")));
            }
            
            return angelOneService.getHoldings(userId)
                    .map(ResponseEntity::ok)
                    .onErrorReturn(ResponseEntity.status(500).body(Map.of("error", "Failed to get holdings")));
        } catch (Exception e) {
            log.error("Error getting AngelOne holdings: ", e);
            return Mono.just(ResponseEntity.status(500).body(Map.of("error", "Internal server error")));
        }
    }

    /**
     * Place an order through AngelOne
     */
    @PostMapping("/order")
    public Mono<ResponseEntity<Map<String, Object>>> placeOrder(
            HttpServletRequest request,
            @RequestBody Map<String, Object> orderRequest) {
        try {
            String userId = authTokenService.getAuthenticatedUserId(request);
            if (userId == null) {
                return Mono.just(ResponseEntity.status(401).body(Map.of("error", "Invalid or missing token")));
            }
            
            return angelOneService.placeOrder(userId, orderRequest)
                    .map(ResponseEntity::ok)
                    .onErrorReturn(ResponseEntity.status(500).body(Map.of("error", "Failed to place order")));
        } catch (Exception e) {
            log.error("Error placing AngelOne order: ", e);
            return Mono.just(ResponseEntity.status(500).body(Map.of("error", "Internal server error")));
        }
    }

    /**
     * Logout from AngelOne
     */
    @PostMapping("/logout")
    public Mono<ResponseEntity<Map<String, Object>>> logout(HttpServletRequest request) {
        try {
            String userId = authTokenService.getAuthenticatedUserId(request);
            if (userId == null) {
                return Mono.just(ResponseEntity.status(401).body(Map.of("error", "Invalid or missing token")));
            }
            
            return angelOneService.logout(userId)
                    .map(response -> {
                        Map<String, Object> result = Map.of(
                                "message", "Successfully logged out from AngelOne",
                                "status", "success"
                        );
                        return ResponseEntity.ok(result);
                    })
                    .onErrorReturn(ResponseEntity.status(500).body(Map.of("error", "Failed to logout")));
        } catch (Exception e) {
            log.error("Error logging out from AngelOne: ", e);
            return Mono.just(ResponseEntity.status(500).body(Map.of("error", "Internal server error")));
        }
    }

    /**
     * Check AngelOne connection status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getConnectionStatus(HttpServletRequest request) {
        try {
            String userId = authTokenService.getAuthenticatedUserId(request);
            if (userId == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid or missing token"));
            }

            boolean isConnected = angelOneService.getTokenForUser(userId).isPresent();
            
            return ResponseEntity.ok(Map.of(
                    "userId", userId,
                    "connected", isConnected,
                    "status", isConnected ? "connected" : "not_connected"
            ));
        } catch (Exception e) {
            log.error("Error checking AngelOne connection status: ", e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }
}
