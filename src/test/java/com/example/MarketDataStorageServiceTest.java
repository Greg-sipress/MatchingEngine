package com.example;

import org.example.matchingengine.config.RedisConfig;
import org.example.matchingengine.model.MarketData;
import org.example.matchingengine.service.MarketDataStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = {MarketDataStorageService.class,
        RedisConfig.class })
class MarketDataStorageServiceTest {

    @Autowired
    private MarketDataStorageService storageService;


    @Test
    void testLastUpdatedTracking() {
        String symbol = "TEST";

        // Initial state
        assertThat(storageService.getLastUpdated(symbol).isEmpty(), is(true));

        // After storing data
        storageService.storeData(symbol, "yahoo", List.of(new MarketData(
                symbol,
                new BigDecimal("123.45"),
                new BigDecimal("122.50"),
                new BigDecimal("124.00"),
                1000L,
                System.currentTimeMillis()
        )));
        assertThat(storageService.getLastUpdated(symbol).isPresent(), is(true));

        // Verify ISO-8601 format
        Instant.parse(storageService.getLastUpdated(symbol).get());
    }
}