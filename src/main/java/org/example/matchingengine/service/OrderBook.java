package org.example.matchingengine.service;

import org.example.matchingengine.model.Trade;
import org.example.matchingengine.model.TradeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderBook {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    TreeMap<Double, PriorityQueue<TradeOrder>> buyMap; // = new TreeMap<>();
    TreeMap<Double, PriorityQueue<TradeOrder>> sellMap; // = new TreeMap<>();

    public ArrayList<Trade> trades = new ArrayList<>();

    volatile double bestBid = 0;
    volatile double bestAsk = 0;


    public OrderBook() {

        buyMap = new TreeMap<>(Collections.reverseOrder());
        sellMap = new TreeMap<>();
    }

    public void setBestBidAsk() {
        if (buyMap.size() > 0) {
            bestBid = buyMap.firstEntry().getKey();
        }
        if (sellMap.size() > 0) {
            bestAsk = sellMap.firstEntry().getKey();
        }
    }
    public void displayOrderBook() {
        System.out.println("Buy Orders:");
        for (var entry : buyMap.entrySet()) {
            System.out.printf("Price: %.2f, Orders: %d\n", entry.getKey(), entry.getValue().size());
            for (TradeOrder order : entry.getValue()) {
                System.out.printf("\tOrderId: %d, Quantity: %d, Timestamp: %d\n", order.getOrderId(), order.getQuantity(), order.getTimestamp());
            }
        }

        System.out.println("Sell Orders:");
        for (var entry : sellMap.entrySet()) {
            System.out.printf("Price: %.2f, Orders: %d\n", entry.getKey(), entry.getValue().size());
            for (TradeOrder order : entry.getValue()) {
                System.out.printf("\tOrderId: %d, Quantity: %d, Timestamp: %d\n", order.getOrderId(), order.getQuantity(), order.getTimestamp());
            }
        }
    }

    public List<TradeOrder> getBids() {
        return buyMap.values().stream().flatMap(Collection::stream).toList();
    }

    public List<TradeOrder> getAsks() {
        return sellMap.values().stream().flatMap(Collection::stream).toList();
    }
   public void processOrder(TradeOrder order) {
        if (order.getMatketLimit().equalsIgnoreCase("market")) {
            trades.add(processMarketOrder(order));
        }
        else {
            addOrder(order);
        }
       setBestBidAsk();
    }


    public void addOrder(TradeOrder order) {
        TreeMap<Double, PriorityQueue<TradeOrder>> book = order.getSide().equals("BUY") ? buyMap : sellMap;
        book.computeIfAbsent(order.getPrice(), k -> new PriorityQueue<>(Comparator.comparingLong(TradeOrder::getTimestamp)))
                .add(order);
    }

    public Trade processMarketOrder(TradeOrder order) {

        Trade trade = null;

        if (order.getSide().equalsIgnoreCase("buy")) {
            TradeOrder bestSellOrder;
            try {
                bestSellOrder = sellMap.firstEntry().getValue().peek();
            }
            catch(NullPointerException e) {
                System.out.println("No sell orders");
                return null;
            }

            assert bestSellOrder != null;
            trade = new Trade.Builder()
                    .withPrice(bestSellOrder.getPrice())
                    .withQuantity(Math.min(bestSellOrder.getQuantity(), order.getQuantity()))
                    .withSymbol(order.getSymbol())
                    .build();
        }
        else {
            TradeOrder bestBuyOrder;
            try {
                 bestBuyOrder =  buyMap.firstEntry().getValue().peek();
            }
            catch (NullPointerException e) {
                System.out.println("No buy orders");
                return null;
            }

            assert bestBuyOrder != null;
            trade = new Trade.Builder()
                    .withPrice(bestBuyOrder.getPrice())
                    .withQuantity(Math.min(bestBuyOrder.getQuantity(), order.getQuantity()))
                    .withSymbol(order.getSymbol())
                    .build();
        }

        return trade;
    }

    // Match bids and asks
    private void matchOrders() {
        while (!buyMap.isEmpty() && !sellMap.isEmpty()) {
            double bestBid = buyMap.firstKey();
            double bestAsk = sellMap.firstKey();

            // If the best bid >= best ask, a trade must occur
            if (bestBid >= bestAsk) {
                int bidQuantity = buyMap.get(bestBid).peek().getQuantity();
                int askQuantity = sellMap.get(bestAsk).peek().getQuantity();

                // Determine the trade quantity (minimum of bid and ask quantities)
                int tradeQuantity = Math.min(bidQuantity, askQuantity);

                // Trade details
                System.out.println("Trade executed:");
                System.out.println("Price: " + bestAsk + ", Quantity: " + tradeQuantity);

                // Update the order books (reduce quantities)
                if (bidQuantity == tradeQuantity) {
                    buyMap.remove(bestBid); // Remove bid completely
                } else {
                   // buyMap.put(bestBid, bidQuantity - tradeQuantity); // Update remaining quantity
                }

                if (askQuantity == tradeQuantity) {
                    sellMap.remove(bestAsk); // Remove ask completely
                } else {
                    //sellMap.put(bestAsk, askQuantity - tradeQuantity); // Update remaining quantity
                }
            } else {
                // If no crossing orders exist, stop the matching process
                break;
            }
        }
    }

}
