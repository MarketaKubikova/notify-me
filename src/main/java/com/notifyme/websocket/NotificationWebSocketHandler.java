package com.notifyme.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket handler for managing WebSocket connections and sending notifications.
 */
@Component
@Slf4j
public class NotificationWebSocketHandler extends TextWebSocketHandler {
    private static final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    /**
     * Called after a new WebSocket connection is established.
     *
     * @param session the WebSocket session
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        sessions.add(session);
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
        sessions.remove(session);
    }

    /**
     * Sends a notification message to all active WebSocket sessions.
     *
     * @param message the notification message to be sent
     */
    public void sendNotification(String message) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("Error sending notification to WebSocket session", e);
            }
        }
    }
}
