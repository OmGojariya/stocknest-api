package com.stocknest.stocknest_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AngelOneConfig {

    @Value("${angelone.api.base-url}")
    private String baseUrl;

    @Value("${angelone.api.client-id}")
    private String clientId;

    @Value("${angelone.api.client-secret}")
    private String clientSecret;

    @Value("${angelone.api.redirect-uri}")
    private String redirectUri;

    @Bean
    public WebClient angelOneWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
