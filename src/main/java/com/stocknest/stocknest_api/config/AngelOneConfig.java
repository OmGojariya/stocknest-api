package com.stocknest.stocknest_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AngelOneConfig {

    @Value("${angelone.api.base-url:https://apiconnect.angelbroking.com}")
    private String baseUrl;

    @Value("${angelone.api.key:AAAQ167570}")
    private String apiKey;

    @Value("${angelone.api.secret:01e94f0d-a641-4224-b57c-728f21c6c413}")
    private String apiSecret;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
