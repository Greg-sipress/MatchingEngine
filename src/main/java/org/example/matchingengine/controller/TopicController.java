package org.example.matchingengine.controller;

import org.example.matchingengine.service.WebSocketNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicController {

    private final WebSocketNotificationService notificationService;

    public TopicController(WebSocketNotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @MessageMapping("/trade")
    @SendTo("/topic/trades")
    public String broadcastTrade(String tradeMessage) {
        return tradeMessage;
    }

    // Maps client messages sent to "/app/tradeOrders" and broadcasts to "/topic/tradeOrders"
    @MessageMapping("/tradeOrder")
    @SendTo("/topic/tradeOrders")
    public String broadcastTradeOrder(String tradeOrderMessage) {
        System.out.println("Received trade order message: " + tradeOrderMessage);
        notificationService.broadcastMessage("/topic/tradeOrders", tradeOrderMessage);
        return tradeOrderMessage; // Send back to all subscribers of "/topic/tradeOrders"
    }

    @MessageMapping("/marketData")
    @SendTo("/topic/marketData")
    public String broadcastMarketData(String marketDataMessage) {
        System.out.println("Received marketData message: " + marketDataMessage);
        notificationService.broadcastMessage("/topic/marketData", marketDataMessage);

        return marketDataMessage;
    }

    @MessageMapping("/privateNotification")
    @SendTo("/queue/notifications")
    public void sendPrivateNotification(String userId, String message) {
        notificationService.sendToUser(userId, "/queue/notifications", message);
    }


}