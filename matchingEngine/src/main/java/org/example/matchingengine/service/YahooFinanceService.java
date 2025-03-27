package org.example.matchingengine.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.matchingengine.model.MarketData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class YahooFinanceService {

    private static final String YAHOO_URL =
            "https://query1.finance.yahoo.com/v8/finance/chart/%s?interval=5m&range=1d";

    public List<MarketData> fetchData(String symbol) throws IOException, InterruptedException {
        String url = String.format(YAHOO_URL, symbol);
        String json = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder(URI.create(url)).build(),
                HttpResponse.BodyHandlers.ofString()
        ).body();

        // Parse JSON response (simplified)
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode candles = root.path("chart").path("result").get(0).path("indicators").path("quote").get(0);

        List<MarketData> data = new ArrayList<>();
        // ... (parse timestamps, OHLC values)
        return data;
    }
}
