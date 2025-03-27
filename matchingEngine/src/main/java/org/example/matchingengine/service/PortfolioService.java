package org.example.matchingengine.service;

import org.example.matchingengine.model.PortfolioItem;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PortfolioService {

    public List<PortfolioItem> getPortfolio() {
        // Static example data, this would typically come from a database or API
        return List.of(
            new PortfolioItem("AAPL", 10, 145.0, 150.0), // Holding 10 shares of Apple
            new PortfolioItem("TSLA", 5, 700.0, 675.0),  // Holding 5 shares of Tesla
            new PortfolioItem("AMZN", 3, 3300.0, 3350.0) // Holding 3 shares of Amazon
        );
    }
}