package com.stocknest.stocknest_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "mails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailData {
    @Id
    private String id;
    private String to;
    private String subject;
    private String body;
    private LocalDateTime sentAt;
}
