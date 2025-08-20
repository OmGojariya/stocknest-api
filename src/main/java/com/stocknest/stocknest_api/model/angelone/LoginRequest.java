package com.stocknest.stocknest_api.model.angelone;

import lombok.Data;

@Data
public class LoginRequest {
    private String clientcode;
    private String password;
    private String totp;
}
