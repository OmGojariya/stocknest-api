package com.stocknest.stocknest_api.model.angelone;

import lombok.Data;

@Data
public class LoginResponse {
    private String jwtToken;
    private String refreshToken;
    private String feedToken;
}
