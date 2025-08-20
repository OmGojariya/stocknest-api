package com.stocknest.stocknest_api.model.angelone;

import lombok.Data;

@Data
public class LTPRequest {
    private String exchange;
    private String tradingsymbol;
    private String symboltoken;
}
