package com.stocknest.stocknest_api.model.angelone;

import lombok.Data;

@Data
public class AngelOneResponse<T> {
    private boolean status;
    private String message;
    private String errorcode;
    private T data;
}
