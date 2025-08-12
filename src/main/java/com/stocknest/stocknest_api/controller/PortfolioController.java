package com.stocknest.stocknest_api.controller;

import com.stocknest.stocknest_api.model.Portfolio;
import com.stocknest.stocknest_api.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    @Autowired
    private final PortfolioService portfolioService;

    @PostMapping
    public Portfolio savePortfolio(@RequestBody Portfolio portfolio) {
        return portfolioService.save(portfolio);
    }

    @GetMapping("/user/{userId}")
    public List<Portfolio> getPortfolioByUser(@PathVariable String userId) {
        return portfolioService.getPortfoliosByUserId(userId);
    }
}
