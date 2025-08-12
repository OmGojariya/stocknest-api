package com.stocknest.stocknest_api.service;

import com.stocknest.stocknest_api.model.Stock;

import java.util.List;
import java.util.Optional;

public interface StockService {
    Stock save(Stock stock);
    List<Stock> getAllStocks();
    Optional<Stock> getBySymbol(String symbol);
    void deleteStock(String symbol);
}
