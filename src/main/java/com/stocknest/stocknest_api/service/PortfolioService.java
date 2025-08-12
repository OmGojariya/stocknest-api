package com.stocknest.stocknest_api.service;

import com.stocknest.stocknest_api.model.Portfolio;

import java.util.List;
import java.util.Optional;

public interface PortfolioService {
    Portfolio save(Portfolio portfolio);
    List<Portfolio> getPortfoliosByUserId(String userId);
    Optional<Portfolio> getPortfolioById(String id);
}
