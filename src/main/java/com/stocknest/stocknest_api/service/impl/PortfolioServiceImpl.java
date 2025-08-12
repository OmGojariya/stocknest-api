package com.stocknest.stocknest_api.service.impl;

import com.stocknest.stocknest_api.model.Portfolio;
import com.stocknest.stocknest_api.repository.PortfolioRepository;
import com.stocknest.stocknest_api.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    private final PortfolioRepository portfolioRepository;

    @Override
    public Portfolio save(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    @Override
    public List<Portfolio> getPortfoliosByUserId(String userId) {
        return portfolioRepository.findByUserId(userId);
    }

    @Override
    public Optional<Portfolio> getPortfolioById(String id) {
        return portfolioRepository.findById(id);
    }
}
