package com.stocknest.stocknest_api.service;

import com.stocknest.stocknest_api.config.AngelOneConfig;
import com.stocknest.stocknest_api.model.AngelOneToken;
import com.stocknest.stocknest_api.repository.AngelOneTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AngelOneService {

    private final WebClient angelOneWebClient;
    private final AngelOneConfig angelOneConfig;
    private final AngelOneTokenRepository tokenRepository;

    /**
     * Generate OAuth authorization URL for AngelOne
     */
    public String getAuthorizationUrl(String state) {
        return angelOneConfig.getBaseUrl() + "/rest/auth/angelbroking/user/v1/loginUrl?" +
                "api_key=" + angelOneConfig.getClientId() +
                "&state=" + state;
    }

    /**
     * Exchange authorization code for access token
     */
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> exchangeCodeForToken(String authCode, String userId) {
        return angelOneWebClient.post()
                .uri("/rest/auth/angelbroking/jwt/v1/generateTokens")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Map.of(
                        "api_key", angelOneConfig.getClientId(),
                        "request_token", authCode,
                        "checksum", generateChecksum(authCode)
                )))
                .retrieve()
                .bodyToMono(Map.class)
                .cast(Map.class)
                .map(response -> (Map<String, Object>) response)
                .doOnSuccess(response -> saveTokens(response, userId))
                .doOnError(error -> log.error("Error exchanging code for token: ", error));
    }

    /**
     * Save tokens to database
     */
    @SuppressWarnings("unchecked")
    private void saveTokens(Map<String, Object> response, String userId) {
        try {
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            
            AngelOneToken token = tokenRepository.findByUserId(userId)
                    .orElse(new AngelOneToken());
                    
            token.setUserId(userId);
            token.setAccessToken((String) data.get("jwtToken"));
            token.setRefreshToken((String) data.get("refreshToken"));
            token.setJwtToken((String) data.get("jwtToken"));
            token.setFeedToken((String) data.get("feedToken"));
            token.setExpiryTime(LocalDateTime.now().plusHours(24)); // AngelOne tokens expire in 24 hours
            token.updateTimestamp();
            
            tokenRepository.save(token);
            log.info("Saved AngelOne tokens for user: {}", userId);
        } catch (Exception e) {
            log.error("Error saving tokens: ", e);
        }
    }

    /**
     * Get stored token for user
     */
    public Optional<AngelOneToken> getTokenForUser(String userId) {
        return tokenRepository.findByUserId(userId);
    }

    /**
     * Get user profile from AngelOne
     */
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> getUserProfile(String userId) {
        Optional<AngelOneToken> tokenOpt = getTokenForUser(userId);
        if (tokenOpt.isEmpty()) {
            return Mono.error(new RuntimeException("No AngelOne token found for user"));
        }

        AngelOneToken token = tokenOpt.get();
        return angelOneWebClient.get()
                .uri("/rest/secure/angelbroking/user/v1/getProfile")
                .header("Authorization", "Bearer " + token.getJwtToken())
                .header("Accept", "application/json")
                .header("X-UserType", "USER")
                .header("X-SourceID", "WEB")
                .header("X-ClientLocalIP", "192.168.1.1")
                .header("X-ClientPublicIP", "192.168.1.1")
                .header("X-MACAddress", "00:00:00:00:00:00")
                .header("X-PrivateKey", angelOneConfig.getClientSecret())
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response);
    }

    /**
     * Place an order
     */
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> placeOrder(String userId, Map<String, Object> orderRequest) {
        Optional<AngelOneToken> tokenOpt = getTokenForUser(userId);
        if (tokenOpt.isEmpty()) {
            return Mono.error(new RuntimeException("No AngelOne token found for user"));
        }

        AngelOneToken token = tokenOpt.get();
        return angelOneWebClient.post()
                .uri("/rest/secure/angelbroking/order/v1/placeOrder")
                .header("Authorization", "Bearer " + token.getJwtToken())
                .header("Accept", "application/json")
                .header("X-UserType", "USER")
                .header("X-SourceID", "WEB")
                .header("X-ClientLocalIP", "192.168.1.1")
                .header("X-ClientPublicIP", "192.168.1.1")
                .header("X-MACAddress", "00:00:00:00:00:00")
                .header("X-PrivateKey", angelOneConfig.getClientSecret())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(orderRequest))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response);
    }

    /**
     * Get holdings
     */
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> getHoldings(String userId) {
        Optional<AngelOneToken> tokenOpt = getTokenForUser(userId);
        if (tokenOpt.isEmpty()) {
            return Mono.error(new RuntimeException("No AngelOne token found for user"));
        }

        AngelOneToken token = tokenOpt.get();
        return angelOneWebClient.get()
                .uri("/rest/secure/angelbroking/portfolio/v1/getHolding")
                .header("Authorization", "Bearer " + token.getJwtToken())
                .header("Accept", "application/json")
                .header("X-UserType", "USER")
                .header("X-SourceID", "WEB")
                .header("X-ClientLocalIP", "192.168.1.1")
                .header("X-ClientPublicIP", "192.168.1.1")
                .header("X-MACAddress", "00:00:00:00:00:00")
                .header("X-PrivateKey", angelOneConfig.getClientSecret())
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response);
    }

    /**
     * Logout and revoke token
     */
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> logout(String userId) {
        Optional<AngelOneToken> tokenOpt = getTokenForUser(userId);
        if (tokenOpt.isEmpty()) {
            return Mono.error(new RuntimeException("No AngelOne token found for user"));
        }

        AngelOneToken token = tokenOpt.get();
        return angelOneWebClient.post()
                .uri("/rest/secure/angelbroking/user/v1/logout")
                .header("Authorization", "Bearer " + token.getJwtToken())
                .header("Accept", "application/json")
                .header("X-UserType", "USER")
                .header("X-SourceID", "WEB")
                .header("X-ClientLocalIP", "192.168.1.1")
                .header("X-ClientPublicIP", "192.168.1.1")
                .header("X-MACAddress", "00:00:00:00:00:00")
                .header("X-PrivateKey", angelOneConfig.getClientSecret())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Map.of("clientcode", angelOneConfig.getClientId())))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response)
                .doOnSuccess(response -> {
                    tokenRepository.deleteByUserId(userId);
                    log.info("Logged out and deleted tokens for user: {}", userId);
                });
    }

    /**
     * Generate checksum for AngelOne API
     */
    private String generateChecksum(String requestToken) {
        // AngelOne requires SHA256 checksum of api_key+request_token+api_secret
        try {
            String data = angelOneConfig.getClientId() + requestToken + angelOneConfig.getClientSecret();
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            log.error("Error generating checksum: ", e);
            return "";
        }
    }
}
