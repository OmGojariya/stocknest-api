package com.stocknest.stocknest_api.model.angelone;

import lombok.Data;

@Data
public class ProfileResponse {
    private String clientcode;
    private String name;
    private String email;
    private String mobileno;
    private String[] exchanges;
    private String[] products;
    private String lastlogintime;
    private String broker;
}
