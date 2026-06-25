package com.nearloo.nearloo_backend.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ToiletStatusHandler {

    private final SimpMessagingTemplate messagingTemplate;

    // Broadcast status update to all subscribed clients
    public void broadcastStatusUpdate(String toiletId, String newStatus) {

    System.out.println(
            "BROADCASTING -> " + toiletId + " : " + newStatus);

    Map<String, String> payload = Map.of(
            "toiletId", toiletId,
            "status", newStatus);

    messagingTemplate.convertAndSend(
            "/topic/toilet-status",
            payload);
}
}
