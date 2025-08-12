package com.stocknest.stocknest_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "download_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadRequest {
    @Id
    private String id;
    private String requestedBy;
    private String fileType;
    private LocalDateTime requestTime;
    private String status;

    public void setRequestedAt(LocalDateTime now) {
    }
}
