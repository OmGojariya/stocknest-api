package com.stocknest.stocknest_api.controller;

import com.stocknest.stocknest_api.model.Stock;
import com.stocknest.stocknest_api.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    @Autowired
    private final StockService stockService;

    @PostMapping
    public Stock saveStock(@RequestBody Stock stock) {
        return stockService.save(stock);
    }

    @GetMapping
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    @GetMapping("/{symbol}")
    public Optional<Stock> getStockById(@PathVariable String symbol) {
        return stockService.getBySymbol(symbol);
    }

    @DeleteMapping("/{symbol}")
    public void deleteStock(@PathVariable String symbol) {
        stockService.deleteStock(symbol);
    }
}
