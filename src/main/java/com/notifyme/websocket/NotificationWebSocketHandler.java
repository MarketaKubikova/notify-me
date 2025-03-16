package com.notifyme.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket handler for managing WebSocket connections and sending notifications.
 */
@Component
@Slf4j
public class NotificationWebSocketHandler extends TextWebSocketHandler {
    // Thread-safe map to store WebSocket sessions and their associated user IDs.
    private static final Map<WebSocketSession, Long> userSessions = new ConcurrentHashMap<>();

    /**
     * Called after a new WebSocket connection is established.
     *
     * @param session the WebSocket session
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws IOException {
        Long userId = extractUserId(session);
        if (userId != null) {
            userSessions.put(session, userId);
            session.sendMessage(new TextMessage("Connected as user " + userId));
        } else {
            session.close();
        }
    }

    /**
     * Handles incoming text messages from WebSocket clients.
     *
     * @param session the WebSocket session
     * @param message the incoming text message
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void handleTextMessage(WebSocketSession session, @NonNull TextMessage message) throws IOException {
        session.sendMessage(new TextMessage("WebSocket connection established!"));
    }

    /**
     * Called after a WebSocket connection is closed.
     *
     * @param session the WebSocket session
     * @param status the close status
     */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull org.springframework.web.socket.CloseStatus status) {
        userSessions.remove(session);
    }

    /**
     * Sends a notification message to a specific user based on their user ID.
     *
     * @param userId the user ID to send the notification to
     * @param message the notification message to be sent
     */
    public void sendNotificationToUser(Long userId, String message) {
        userSessions.forEach((session, storedUserId) -> {
            if (storedUserId.equals(userId)) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    log.error("Error sending notification to WebSocket session", e);
                }
            }
        });
    }

    /**
     * Extracts the user ID from the WebSocket session's query parameters.
     *
     * @param session the WebSocket session
     * @return the extracted user ID, or null if extraction fails
     */
    private Long extractUserId(WebSocketSession session) {
        String query = Objects.requireNonNull(session.getUri()).getQuery();
        if (query != null && query.startsWith("userId=")) {
            try {
                return Long.parseLong(query.split("=")[1]);
            } catch (NumberFormatException e) {
                log.error("Error parsing user id from websocket session", e);
            }
        }
        return null;
    }
}
