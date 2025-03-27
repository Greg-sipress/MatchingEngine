package org.example.matchingengine.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class TradeOrder {

    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long orderId;
    private double price;
    private int quantity;
    private long timestamp;
    private String side; // "BUY" or "SELL"
    private String symbol;
    private String marketLimit;




    public TradeOrder() {}


    private TradeOrder(Builder builder) {
        this.orderId = builder.orderId;
        this.price = builder.price;
        this.quantity = builder.quantity;
        this.timestamp = builder.timestamp;
        this.side = builder.side;
        this.symbol = builder.symbol;
        this.marketLimit = builder.marketLimit;
    }


    public long getOrderId() {
        return orderId;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSide() {
        return side;
    }
    public String getSymbol() {
        return symbol;
    }
    public String getMatketLimit() {
        return marketLimit;
    }


    public String toJson() {
        return String.format(
                "{\"orderId\":%d,\"price\":%.2f,\"quantity\":%d,\"timestamp\":\"%s\",\"side\":\"%s\",\"symbol\":\"%s\",\"marketLimit\":\"%s\"}",
                orderId, price, quantity, timestamp, side, symbol, marketLimit
        );
    }


    public static class Builder {
        private int orderId;
        private double price;
        private int quantity;
        private long timestamp;
        private String side; // "BUY" or "SELL"
        private String symbol;
        private String marketLimit;

        public Builder orderId(int orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder side(String side) {
            this.side = side;
            return this;
        }
        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }
        public Builder marketLimit(String marketLimit) {
            this.marketLimit = marketLimit;
            return this;
        }

        public TradeOrder build() {
            validate(); // Validation before building
            return new TradeOrder(this );
        }



        private void validate() {
            if (orderId == 0) {
                throw new IllegalArgumentException("Order ID cannot be null or empty");
            }
            if (!"BUY".equals(side) && !"SELL".equals(side)) {
                throw new IllegalArgumentException("Side must be either 'BUY' or 'SELL'");
            }
            if (price <= 0) {
                throw new IllegalArgumentException("Price must be greater than zero");
            }
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }
        }
    }

}
