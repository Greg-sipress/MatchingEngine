package org.example.matchingengine.controller;


import org.example.matchingengine.components.MarketDataGenerator;
import org.example.matchingengine.model.MarketData;
import org.example.matchingengine.service.MarketDataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/market")
public class MarketDataController {

    @Autowired
    private MarketDataStorageService storageService;

    @Autowired
    private MarketDataGenerator marketDataGenerator;

    @PostMapping("/simulation/start")
    public String startSimulation() {

        storageService.clearDataStore();


        marketDataGenerator.startSim();
        return "Started simulation";
    }

    @PostMapping("/simulation/stop")
    public String stopSimulation() {

        marketDataGenerator.stopSim();
        return "Stopped simulation";
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<Map<String, Object>> getMarketData(
            @PathVariable String symbol) {

        Map<String, Object> response = new HashMap<>();
        response.put("symbol", symbol);
        response.put("last_updated",
                storageService.getLastUpdated(symbol).orElse("N/A"));

        storageService.getData(symbol, "yahoo").ifPresent(data ->
                response.put("yahoo", data));
        storageService.getData(symbol, "auto").ifPresent(data ->
                response.put("auto", data));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{symbol}/latest")
    public ResponseEntity<Map<String, Object>> getLatestPrice(
            @PathVariable String symbol) {

        // Try Alpha Vantage first, fallback to Yahoo
        Optional<List<MarketData>> data = storageService.getData(symbol, "auto");

        if (data.isEmpty() || data.get().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MarketData latest = data.get().get(data.get().size() - 1);
        return ResponseEntity.ok(Map.of(
                "symbol", symbol,
                "price", latest.getPrice(),
                "time", latest.getTimestamp()
        ));
    }
}
