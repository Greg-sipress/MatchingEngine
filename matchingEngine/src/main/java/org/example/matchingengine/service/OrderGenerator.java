package org.example.matchingengine.service;

import org.example.matchingengine.Main;
import org.example.matchingengine.model.TradeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderGenerator {


    //private SimpMessagingTemplate template;
    @Autowired
    private WebSocketNotificationService notificationService;



    public OrderBook orderBook;


    public OrderGenerator() {
        //this.template = simp;
        //this.template = template;
        this.orderBook = new OrderBook();

    }

    public void begin() {
        generateOrders();
        //orderBook.displayOrderBook();
        publishTrades();
    }

    public void generateOrders() {

        long start = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {

            TradeOrder tradeOrder = generateTradeOrder();
            orderBook.processOrder(tradeOrder);
            //notificationService.broadcastMessage("/topic/trades", tradeOrder.toString());
            notificationService.broadcastMessage("/topic/tradeOrders", tradeOrder.toString());

            //orderBook.processOrder(generateTradeOrder());

        }
        long end = System.currentTimeMillis();

        System.out.println("Time taken to generate " + 1_000_000 + " orders: " + (end - start) + " ms");

        
    }

    public void publishTrades() {

        if (orderBook.trades == null || orderBook.trades.isEmpty() || orderBook.trades.size() < 10) {
            return;
        }
        orderBook.trades.stream().limit(10).forEach(trade -> {
            if (trade != null) {
                String tradeUpdate = "Trade: " + trade.toString();
                executeTrade(tradeUpdate);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        });
    }

    public void executeTrade(String tradeMessage) {
        // Broadcast the trade message to all subscribers of "/topic/trades"
        notificationService.broadcastMessage("/topic/trades", tradeMessage);
    }

    TradeOrder generateTradeOrder() {

        String side = Math.random() > 0.5 ? "BUY" : "SELL";
        String marketLimit = Math.random() > 0.8 ? "MARKET" : "LIMIT";

        TradeOrder tradeOrder = new TradeOrder.Builder()
                .orderId(1 + (int) (Math.random() * 1000000))

                .quantity(10 + (int) (Math.random() * 100))
                .timestamp(System.currentTimeMillis())
                .side(side)
                .marketLimit(marketLimit)

                .price(Math.round(((side.equals("BUY") && marketLimit.equals("LIMIT")) ? Math.random() * 50 : Math.random() * 100) * 10) / 10.0)
                .symbol("VOO")
                .build();

        return tradeOrder;
    }
}
