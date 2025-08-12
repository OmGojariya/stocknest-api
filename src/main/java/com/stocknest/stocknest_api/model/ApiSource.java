package com.stocknest.stocknest_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "api_sources")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiSource {
    @Id
    private String id;
    private String providerName;
    private String apiKey;
    private String baseUrl;
    private boolean active;
}
