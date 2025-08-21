package com.stocknest.stocknest_api.service;

import com.stocknest.stocknest_api.model.angelone.*;
import com.stocknest.stocknest_api.model.AngelOneToken;
import com.stocknest.stocknest_api.repository.AngelOneTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@Slf4j
public class AngelOneService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AngelOneTokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${angelone.api.key:AAAQ167570}")
    private String apiKey;

    @Value("${angelone.api.secret:01e94f0d-a641-4224-b57c-728f21c6c413}")
    private String apiSecret;

    private static final String BASE_URL = "https://apiconnect.angelbroking.com";

    /**
     * Login to AngelOne using credentials
     */
    public AngelOneResponse<LoginResponse> login(LoginRequest loginRequest) {
        try {
            String url = BASE_URL + "/rest/auth/angelbroking/user/v1/loginByPassword";

            HttpHeaders headers = createHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(),
                    new TypeReference<Map<String, Object>>() {
                    });

            AngelOneResponse<LoginResponse> result = new AngelOneResponse<>();
            result.setStatus((Boolean) responseMap.get("status"));
            result.setMessage((String) responseMap.get("message"));
            result.setErrorcode((String) responseMap.get("errorcode"));

            if (result.isStatus() && responseMap.get("data") != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setJwtToken((String) data.get("jwtToken"));
                loginResponse.setRefreshToken((String) data.get("refreshToken"));
                loginResponse.setFeedToken((String) data.get("feedToken"));
                result.setData(loginResponse);

                // Save tokens to database
                saveTokens(data, loginRequest.getClientcode());
            }

            return result;
        } catch (Exception e) {
            log.error("Error during login: ", e);
            AngelOneResponse<LoginResponse> errorResponse = new AngelOneResponse<>();
            errorResponse.setStatus(false);
            errorResponse.setMessage("Login failed: " + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * Get user profile
     */
    public AngelOneResponse<ProfileResponse> getProfile(String userId) {
        try {
            Optional<AngelOneToken> tokenOpt = getTokenForUser(userId);
            if (tokenOpt.isEmpty()) {
                AngelOneResponse<ProfileResponse> errorResponse = new AngelOneResponse<>();
                errorResponse.setStatus(false);
                errorResponse.setMessage("No token found for user");
                return errorResponse;
            }

            String url = BASE_URL + "/rest/secure/angelbroking/user/v1/getProfile";
            HttpHeaders headers = createAuthenticatedHeaders(tokenOpt.get().getJwtToken());

            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(),
                    new TypeReference<Map<String, Object>>() {
                    });

            AngelOneResponse<ProfileResponse> result = new AngelOneResponse<>();
            result.setStatus((Boolean) responseMap.get("status"));
            result.setMessage((String) responseMap.get("message"));
            result.setErrorcode((String) responseMap.get("errorcode"));

            if (result.isStatus() && responseMap.get("data") != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
                ProfileResponse profileResponse = objectMapper.convertValue(data, ProfileResponse.class);
                result.setData(profileResponse);
            }

            return result;
        } catch (Exception e) {
            log.error("Error getting profile: ", e);
            AngelOneResponse<ProfileResponse> errorResponse = new AngelOneResponse<>();
            errorResponse.setStatus(false);
            errorResponse.setMessage("Get profile failed: " + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * Place order
     */
    public AngelOneResponse<OrderResponse> placeOrder(String userId, OrderRequest orderRequest) {
        try {
            Optional<AngelOneToken> tokenOpt = getTokenForUser(userId);
            if (tokenOpt.isEmpty()) {
                AngelOneResponse<OrderResponse> errorResponse = new AngelOneResponse<>();
                errorResponse.setStatus(false);
                errorResponse.setMessage("No token found for user");
                return errorResponse;
            }

            String url = BASE_URL + "/rest/secure/angelbroking/order/v1/placeOrder";
            HttpHeaders headers = createAuthenticatedHeaders(tokenOpt.get().getJwtToken());

            HttpEntity<OrderRequest> request = new HttpEntity<>(orderRequest, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(),
                    new TypeReference<Map<String, Object>>() {
                    });

            AngelOneResponse<OrderResponse> result = new AngelOneResponse<>();
            result.setStatus((Boolean) responseMap.get("status"));
            result.setMessage((String) responseMap.get("message"));
            result.setErrorcode((String) responseMap.get("errorcode"));

            if (result.isStatus() && responseMap.get("data") != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
                OrderResponse orderResponse = objectMapper.convertValue(data, OrderResponse.class);
                result.setData(orderResponse);
            }

            return result;
        } catch (Exception e) {
            log.error("Error placing order: ", e);
            AngelOneResponse<OrderResponse> errorResponse = new AngelOneResponse<>();
            errorResponse.setStatus(false);
            errorResponse.setMessage("Place order failed: " + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * Get holdings
     */
    public AngelOneResponse<List<HoldingResponse>> getHoldings(String userId) {
        try {
            Optional<AngelOneToken> tokenOpt = getTokenForUser(userId);
            if (tokenOpt.isEmpty()) {
                AngelOneResponse<List<HoldingResponse>> errorResponse = new AngelOneResponse<>();
                errorResponse.setStatus(false);
                errorResponse.setMessage("No token found for user");
                return errorResponse;
            }

            String url = BASE_URL + "/rest/secure/angelbroking/portfolio/v1/getHolding";
            HttpHeaders headers = createAuthenticatedHeaders(tokenOpt.get().getJwtToken());

            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(),
                    new TypeReference<Map<String, Object>>() {
                    });

            AngelOneResponse<List<HoldingResponse>> result = new AngelOneResponse<>();
            result.setStatus((Boolean) responseMap.get("status"));
            result.setMessage((String) responseMap.get("message"));
            result.setErrorcode((String) responseMap.get("errorcode"));

            if (result.isStatus() && responseMap.get("data") != null) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) responseMap.get("data");
                List<HoldingResponse> holdings = dataList.stream()
                        .map(data -> objectMapper.convertValue(data, HoldingResponse.class))
                        .toList();
                result.setData(holdings);
            }

            return result;
        } catch (Exception e) {
            log.error("Error getting holdings: ", e);
            AngelOneResponse<List<HoldingResponse>> errorResponse = new AngelOneResponse<>();
            errorResponse.setStatus(false);
            errorResponse.setMessage("Get holdings failed: " + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * Get LTP (Last Traded Price)
     */
    public AngelOneResponse<LTPResponse> getLTP(String userId, LTPRequest ltpRequest) {
        try {
            Optional<AngelOneToken> tokenOpt = getTokenForUser(userId);
            if (tokenOpt.isEmpty()) {
                AngelOneResponse<LTPResponse> errorResponse = new AngelOneResponse<>();
                errorResponse.setStatus(false);
                errorResponse.setMessage("No token found for user");
                return errorResponse;
            }

            String url = BASE_URL + "/rest/secure/angelbroking/order/v1/getLTP";
            HttpHeaders headers = createAuthenticatedHeaders(tokenOpt.get().getJwtToken());

            HttpEntity<LTPRequest> request = new HttpEntity<>(ltpRequest, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(),
                    new TypeReference<Map<String, Object>>() {
                    });

            AngelOneResponse<LTPResponse> result = new AngelOneResponse<>();
            result.setStatus((Boolean) responseMap.get("status"));
            result.setMessage((String) responseMap.get("message"));
            result.setErrorcode((String) responseMap.get("errorcode"));

            if (result.isStatus() && responseMap.get("data") != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
                LTPResponse ltpResponse = objectMapper.convertValue(data, LTPResponse.class);
                result.setData(ltpResponse);
            }

            return result;
        } catch (Exception e) {
            log.error("Error getting LTP: ", e);
            AngelOneResponse<LTPResponse> errorResponse = new AngelOneResponse<>();
            errorResponse.setStatus(false);
            errorResponse.setMessage("Get LTP failed: " + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * Logout user
     */
    public AngelOneResponse<String> logout(String userId) {
        try {
            Optional<AngelOneToken> tokenOpt = getTokenForUser(userId);
            if (tokenOpt.isEmpty()) {
                AngelOneResponse<String> errorResponse = new AngelOneResponse<>();
                errorResponse.setStatus(false);
                errorResponse.setMessage("No token found for user");
                return errorResponse;
            }

            String url = BASE_URL + "/rest/secure/angelbroking/user/v1/logout";
            HttpHeaders headers = createAuthenticatedHeaders(tokenOpt.get().getJwtToken());

            Map<String, String> logoutRequest = new HashMap<>();
            logoutRequest.put("clientcode", userId);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(logoutRequest, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(),
                    new TypeReference<Map<String, Object>>() {
                    });

            AngelOneResponse<String> result = new AngelOneResponse<>();
            result.setStatus((Boolean) responseMap.get("status"));
            result.setMessage((String) responseMap.get("message"));
            result.setErrorcode((String) responseMap.get("errorcode"));

            if (result.isStatus()) {
                // Delete tokens from database
                tokenRepository.deleteByUserId(userId);
                result.setData("Logged out successfully");
            }

            return result;
        } catch (Exception e) {
            log.error("Error during logout: ", e);
            AngelOneResponse<String> errorResponse = new AngelOneResponse<>();
            errorResponse.setStatus(false);
            errorResponse.setMessage("Logout failed: " + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * Get stored token for user
     */
    private Optional<AngelOneToken> getTokenForUser(String userId) {
        return tokenRepository.findByUserId(userId);
    }

    /**
     * Save tokens to database
     */
    private void saveTokens(Map<String, Object> data, String userId) {
        try {
            AngelOneToken token = tokenRepository.findByUserId(userId)
                    .orElse(new AngelOneToken());

            token.setUserId(userId);
            token.setJwtToken((String) data.get("jwtToken"));
            token.setRefreshToken((String) data.get("refreshToken"));
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
     * Create basic headers
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        headers.set("X-UserType", "USER");
        headers.set("X-SourceID", "WEB");
        headers.set("X-ClientLocalIP", "192.168.1.1");
        headers.set("X-ClientPublicIP", "192.168.1.1");
        headers.set("X-MACAddress", "00:00:00:00:00:00");
        headers.set("X-PrivateKey", apiKey);
        return headers;
    }

    /**
     * Create authenticated headers
     */
    private HttpHeaders createAuthenticatedHeaders(String jwtToken) {
        HttpHeaders headers = createHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        return headers;
    }
}
