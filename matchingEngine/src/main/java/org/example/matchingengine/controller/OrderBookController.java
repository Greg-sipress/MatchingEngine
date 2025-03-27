package org.example.matchingengine.controller;

import org.example.matchingengine.model.TradeOrder;
import org.example.matchingengine.service.OrderBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order-book")
public class OrderBookController {

    private final OrderBook orderBook;

    @Autowired
    public OrderBookController(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    // Expose the final order book
    @GetMapping("/final")
    public Map<String, List<TradeOrder>> getFinalOrderBook() {
        // Assuming OrderBook has methods to fetch bid and ask maps
        return Map.of(
            "bids", orderBook.getBids(), // Get bid prices and quantities
            "asks", orderBook.getAsks()  // Get ask prices and quantities
        );
    }
}