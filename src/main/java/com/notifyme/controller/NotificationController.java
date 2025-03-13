package com.notifyme.controller;

import com.notifyme.service.NotificationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling notification-related requests.
 */
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    /**
     * Service for producing notifications and sending them to a Kafka topic.
     */
    private final NotificationProducer notificationProducer;

    /**
     * Endpoint for sending a notification message to the Kafka topic.
     *
     * @param message the notification message to be sent
     * @return a ResponseEntity with a success message
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestParam String message) {
        notificationProducer.sendNotification("notifications", message);
        return ResponseEntity.ok("Notification sent");
    }
}
