package org.example.matchingengine.controller;

import org.example.matchingengine.model.PortfolioItem;
import org.example.matchingengine.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping
    public List<PortfolioItem> getPortfolio() {
        return portfolioService.getPortfolio(); // Fetch all portfolio holdings
    }
}