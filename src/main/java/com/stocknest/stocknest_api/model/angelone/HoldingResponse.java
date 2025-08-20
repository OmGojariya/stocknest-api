package com.stocknest.stocknest_api.model.angelone;

import lombok.Data;

@Data
public class HoldingResponse {
    private String tradingsymbol;
    private String exchange;
    private String isin;
    private String t1quantity;
    private String totalquantity;
    private String authorisedquantity;
    private String profitandloss;
    private String product;
    private String collateralquantity;
    private String collateraltype;
    private String haircut;
    private String averageprice;
    private String ltp;
    private String symboltoken;
    private String close;
}
