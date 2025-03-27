package org.example.matchingengine.service;
import org.example.matchingengine.model.MarketData;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class MarketDataStorageService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public MarketDataStorageService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Existing methods...

    public Optional<String> getLastUpdated(String symbol) {
        String key = String.format("market:%s:last_updated", symbol);
        return Optional.ofNullable(
                (String) redisTemplate.opsForValue().get(key)
        );
    }

    public void storeData(String symbol, String source, List<MarketData> data) {
        String key = String.format("market:%s:%s", symbol, source);
        redisTemplate.opsForValue().set(key, data);

        // Set TTL (1 hour)
        redisTemplate.expire(key, 1, TimeUnit.HOURS);

        // Update last modified timestamp
        redisTemplate.opsForValue().set(
                String.format("market:%s:last_updated", symbol),
                Instant.now().toString()
        );
    }

    public void storeData(String symbol, String source, MarketData data) {
        String key = String.format("market:%s:%s", symbol, source);
        redisTemplate.opsForValue().set(key, data);

        // Set TTL (1 hour)
        redisTemplate.expire(key, 1, TimeUnit.HOURS);

        // Update last modified timestamp
        redisTemplate.opsForValue().set(
                String.format("market:%s:last_updated", symbol),
                Instant.now().toString()
        );
    }

    public Optional<List<MarketData>> getData(String symbol, String source) {
        String key = String.format("market:%s:%s", symbol, source);
        return Optional.ofNullable(
                (List<MarketData>) redisTemplate.opsForValue().get(key)
        );
    }

    public void storeDataAtomic(String symbol, String source, List<MarketData> data) {
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) {
                operations.multi();

                String dataKey = String.format("market:%s:%s", symbol, source);
                operations.opsForValue().set(dataKey, data);
                operations.expire(dataKey, 1, TimeUnit.HOURS);

                String timestampKey = String.format("market:%s:last_updated", symbol);
                operations.opsForValue().set(timestampKey, Instant.now().toString());
                operations.expire(timestampKey, 1, TimeUnit.HOURS);

                return operations.exec();
            }
        });
    }

    // Clear the Redis store
    public void clearDataStore() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().flushDb();
    }

}