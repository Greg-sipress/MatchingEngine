package org.example.matchingengine.model;

import lombok.Data;
import jakarta.persistence.Entity;

@Data
@Entity
public class Trade {

    protected Trade() {
        // Required by JPA
    }

    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private  double price;
    private  double quantity;
    private  long timestamp;
    private  int marketOrderId;
    private  int limitOrderId;
    private  String symbol;
    private  int buyerId;
    private  int sellerId;

    private Trade(Builder builder) {
        this.price = builder.price;
        this.quantity = builder.quantity;
        this.timestamp = builder.timestamp;
        this.marketOrderId = builder.marketOrderId;
        this.limitOrderId = builder.limitOrderId;
        this.symbol = builder.symbol;
        this.buyerId = builder.buyerId;
        this.sellerId = builder.sellerId;
        this.id = builder.id;
    }

    public Trade(int id, int buyer, int seller, Double price, Integer quantity, long timestamp, int marketOrderId, int limitOrderId, String symbol) {
        this.id = id;
        this.buyerId = buyer;
        this.sellerId = seller;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.marketOrderId = marketOrderId;
        this.limitOrderId = limitOrderId;
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getMarketOrderId() {
        return marketOrderId;
    }

    public int getLimitOrderId() {
        return limitOrderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public String toString() {
        return toJson();
    }

    public String toJson() {
        return String.format(
                "{\"id\":%d,\"price\":%.2f,\"quantity\":%.2f,\"timestamp\":\"%s\",\"marketOrderId\":%d,\"limitOrderId\":%d,\"symbol\":\"%s\",\"buyerId\":%d,\"sellerId\":%d}",
                id, price, quantity, timestamp, marketOrderId, limitOrderId, symbol, buyerId, sellerId
        );
    }


    public static class Builder {
        private double price;
        private double quantity;
        private long timestamp;
        private int marketOrderId;
        private int limitOrderId;
        private String symbol;
        private int buyerId;
        private int sellerId;
        private int id;

        public Builder withPrice(double price) {
            this.price = price;
            return this;
        }

        public Builder withQuantity(double quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder withTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withMarketOrderId(int marketOrderId) {
            this.marketOrderId = marketOrderId;
            return this;
        }

        public Builder withLimitOrderId(int limitOrderId) {
            this.limitOrderId = limitOrderId;
            return this;
        }

        public Builder withSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder withBuyerId(int buyerId) {
            this.buyerId = buyerId;
            return this;
        }

        public Builder withSellerId(int sellerId) {
            this.sellerId = sellerId;
            return this;
        }
        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Trade build() {
            validate(); // Add validation logic
            return new Trade(this);
        }

        private void validate() {
            if (price <= 0) {
                throw new IllegalArgumentException("Price must be greater than 0");
            }
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }
            if (symbol == null || symbol.isEmpty()) {
                throw new IllegalArgumentException("Symbol must not be null or empty");
            }
        }
    }

}
