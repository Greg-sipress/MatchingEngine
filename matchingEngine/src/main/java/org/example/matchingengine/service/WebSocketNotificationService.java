package org.example.matchingengine.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Broadcasts a message to all clients subscribed to a topic.
     */
    public void broadcastMessage(String destination, Object payload) {
        messagingTemplate.convertAndSend(destination, payload); // Sends to destination (e.g., /topic/trades)
    }

    /**
     * Sends a message to a specific user.
     */
    public void sendToUser(String username, String destination, Object payload) {
        messagingTemplate.convertAndSendToUser(username, destination, payload);
    }
}