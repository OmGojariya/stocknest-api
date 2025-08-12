package com.stocknest.stocknest_api.service.impl;

import com.stocknest.stocknest_api.model.Stock;
import com.stocknest.stocknest_api.repository.StockRepository;
import com.stocknest.stocknest_api.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    @Autowired
    private final StockRepository stockRepository;

    @Override
    public Stock save(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Override
    public Optional<Stock> getBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }

    @Override
    public void deleteStock(String symbol) {
        stockRepository.deleteBySymbol(symbol);
    }
}
