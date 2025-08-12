package com.stocknest.stocknest_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Holding {
    private String stockSymbol;
    private int quantity;
    private double averageBuyPrice;
    private LocalDateTime buyDate;
}
