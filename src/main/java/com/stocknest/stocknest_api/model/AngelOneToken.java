package com.stocknest.stocknest_api.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "angelone_tokens")
@Data
public class AngelOneToken {
    @Id
    private String id;
    private String userId;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiryTime;
    private String jwtToken;
    private String feedToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AngelOneToken() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
