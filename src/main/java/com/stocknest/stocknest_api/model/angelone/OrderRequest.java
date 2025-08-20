package com.stocknest.stocknest_api.model.angelone;

import lombok.Data;

@Data
public class OrderRequest {
    private String variety;
    private String tradingsymbol;
    private String symboltoken;
    private String transactiontype;
    private String exchange;
    private String ordertype;
    private String producttype;
    private String duration;
    private String price;
    private String squareoff;
    private String stoploss;
    private String quantity;
}
