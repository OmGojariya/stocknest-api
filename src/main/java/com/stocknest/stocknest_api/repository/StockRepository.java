package com.stocknest.stocknest_api.repository;

import com.stocknest.stocknest_api.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StockRepository extends MongoRepository<Stock,String> {
    Optional<Stock> findBySymbol(String symbol);
    void deleteBySymbol(String symbol);

}
