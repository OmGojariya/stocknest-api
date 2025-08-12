package com.stocknest.stocknest_api.repository;

import com.mongodb.client.MongoDatabase;
import com.stocknest.stocknest_api.model.Portfolio;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PortfolioRepository extends MongoRepository<Portfolio,String> {
    List<Portfolio> findByUserId(String userId);
}
