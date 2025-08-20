package com.stocknest.stocknest_api.controller;

import com.stocknest.stocknest_api.model.angelone.*;
import com.stocknest.stocknest_api.service.AngelOneService;
import com.stocknest.stocknest_api.service.AuthTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/angelone")
@RequiredArgsConstructor
@Slf4j
public class AngelOneController {

    private final AngelOneService angelOneService;
    private final AuthTokenService authTokenService;

    /**
     * Direct login to AngelOne using credentials
     */
    @PostMapping("/login")
    public ResponseEntity<AngelOneResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        try {
            AngelOneResponse<LoginResponse> response = angelOneService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error during AngelOne login: ", e);
            AngelOneResponse<LoginResponse> errorResponse = new AngelOneResponse<>();
            errorResponse.setStatus(false);
            errorResponse.setMessage("Login failed: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Get user profile from AngelOne
     */
    @GetMapping("/profile")
    public ResponseEntity<AngelOneResponse<ProfileResponse>> getUserProfile(HttpServletRequest request) {
        try {
            String userId = authTokenService.getAuthenticatedUserId(request);
            if (userId == null) {
                AngelOneResponse<ProfileResponse> errorResponse = new AngelOneResponse<>();
                errorResponse.setStatus(false);
                errorResponse.setMessage("Invalid or missing token");
                return ResponseEntity.status(401).body(errorResponse);
            }

            AngelOneResponse<ProfileResponse> response = angelOneService.getProfile(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting AngelOne profile: ", e);
            AngelOneResponse<ProfileResponse> errorResponse = new AngelOneResponse<>();
            errorResponse.setStatus(false);
            errorResponse.setMessage("Get profile failed: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Get holdings from AngelOne
     */
    @GetMapping("/holdings")
    public ResponseEntity<AngelOneResponse<List<HoldingResponse>>> getHoldings(HttpServletRequest request) {
        try {
            String userId = authTokenService.getAuthenticatedUserId(request);
            if (userId == null) {
                AngelOneResponse<List<HoldingResponse>> errorResponse = new AngelOneResponse<>();
                errorResponse.setStatus(false);
                errorResponse.setMessage("Invalid or missing token");
                return ResponseEntity.status(401).body(errorResponse);
            }

            AngelOneResponse<List<HoldingResponse>> response = angelOneService.getHoldings(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting AngelOne holdings: ", e);
            AngelOneResponse<List<HoldingResponse>> errorResponse = new AngelOneResponse<>();
            errorResponse.setStatus(false);
            errorResponse.setMessage("Get holdings failed: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Place an order through AngelOne
     */
    @PostMapping("/order")
    public ResponseEntity<AngelOneResponse<OrderResponse>> placeOrder(
            HttpServletRequest request,
            @RequestBody OrderRequest orderRequest) {
        try {
            String userId = authTokenService.getAuthenticatedUserId(request);
            if (userId == null) {
                AngelOneResponse<OrderResponse> errorResponse = new AngelOneResponse<>();
                errorResponse.setStatus(false);
                errorResponse.setMessage("Invalid or missing token");
                return ResponseEntity.status(401).body(errorResponse);
            }

            AngelOneResponse<OrderResponse> response = angelOneService.placeOrder(userId, orderRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error placing AngelOne order: ", e);
            AngelOneResponse<OrderResponse> errorResponse = new AngelOneResponse<>();
            errorResponse.setStatus(false);
            errorResponse.setMessage("Place order failed: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Get LTP (Last Traded Price)
     */
    @PostMapping("/ltp")
    public ResponseEntity<AngelOneResponse<LTPResponse>> getLTP(
            HttpServletRequest request,
            @RequestBody LTPRequest ltpRequest) {
        try {
            String userId = authTokenService.getAuthenticatedUserId(request);
            if (userId == null) {
                AngelOneResponse<LTPResponse> errorResponse = new AngelOneResponse<>();
                errorResponse.setStatus(false);
                errorResponse.setMessage("Invalid or missing token");
                return ResponseEntity.status(401).body(errorResponse);
            }

            AngelOneResponse<LTPResponse> response = angelOneService.getLTP(userId, ltpRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting LTP: ", e);
            AngelOneResponse<LTPResponse> errorResponse = new AngelOneResponse<>();
            errorResponse.setStatus(false);
            errorResponse.setMessage("Get LTP failed: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Logout from AngelOne
     */
    @PostMapping("/logout")
    public ResponseEntity<AngelOneResponse<String>> logout(HttpServletRequest request) {
        try {
            String userId = authTokenService.getAuthenticatedUserId(request);
            if (userId == null) {
                AngelOneResponse<String> errorResponse = new AngelOneResponse<>();
                errorResponse.setStatus(false);
                errorResponse.setMessage("Invalid or missing token");
                return ResponseEntity.status(401).body(errorResponse);
            }

            AngelOneResponse<String> response = angelOneService.logout(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error logging out from AngelOne: ", e);
            AngelOneResponse<String> errorResponse = new AngelOneResponse<>();
            errorResponse.setStatus(false);
            errorResponse.setMessage("Logout failed: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Handle AngelOne postback (webhooks)
     */
    @PostMapping("/postback")
    public ResponseEntity<Map<String, String>> handlePostback(@RequestBody Map<String, Object> postbackData) {
        log.info("AngelOne postback received: {}", postbackData);
        // Process postback data (order updates, etc.)
        return ResponseEntity.ok(Map.of("status", "acknowledged"));
    }
}
