package org.example.matchingengine.components;


import org.example.matchingengine.model.MarketData;
import org.example.matchingengine.service.MarketDataStorageService;
import org.example.matchingengine.service.WebSocketNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class MarketDataGenerator {

    private final AtomicBoolean running = new AtomicBoolean(false);

    @Autowired
    private WebSocketNotificationService notificationService;

    private final Random random = new Random();
    private final List<String> symbols = List.of(
            "AAPL", "MSFT", "GOOGL", "AMZN", "TSLA",
            "NVDA", "META", "NFLX", "SPY", "QQQ"
    );

    @Autowired
    private transient MarketDataStorageService storageService;


    private final Map<String, Double> lastPrices = new ConcurrentHashMap<>();

    public void startSim() {



        running.set(true);

        while (running.get()) {

            for (String symbol : symbols) {

                List<MarketData> marketDataList = new ArrayList<>();

                for (int i = 0; i < 10; i++) {

                    MarketData marketData = generate(symbol);
                    marketDataList.add(marketData);
                    notificationService.broadcastMessage("/topic/marketData", marketData);

                    //marketDataList.add(generate(symbol));


                }
                storageService.storeData(symbol, "auto", marketDataList);

            }

        }


    }

    public void stopSim() {

        running.set(false);
    }

    public MarketData generate(String symbol) {
        double lastPrice = lastPrices.getOrDefault(symbol, 100.0 + random.nextDouble() * 100);
        double change = (random.nextGaussian() * 0.5); // Normally distributed changes
        double newPrice = lastPrice * (1 + change/100);
        long volume = 1000 + random.nextInt(99000);

        lastPrices.put(symbol, newPrice);

        BigDecimal temp = new BigDecimal(newPrice);
        return new MarketData(
                symbol,
                temp,
                temp,
                temp,
                volume,
                System.currentTimeMillis()
        );
    }

    public List<String> getSymbols() {
        return symbols;
    }
}