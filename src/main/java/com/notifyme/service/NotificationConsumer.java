package com.notifyme.service;

import com.notifyme.model.Notification;
import com.notifyme.repository.NotificationRepository;
import com.notifyme.websocket.NotificationWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service class responsible for consuming notifications from a Kafka topic.
 */
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    // Initializes an ExecutorService with a fixed thread pool of four threads.
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final NotificationRepository notificationRepository;
    private final NotificationWebSocketHandler notificationWebSocketHandler;

    /**
     * Listens to the specified Kafka topic and submits the received messages for processing.
     *
     * @param consumerRecord the record received from the Kafka topic
     */
    @KafkaListener(topics = "notifications", groupId = "notification-group")
    public void listen(ConsumerRecord<String, String> consumerRecord) {
        executorService.submit(() -> processMessage(consumerRecord.value()));
    }

    /**
     * Processes the received notification message.
     *
     * @param message the notification message to be processed
     */
    private void processMessage(String message) {
        System.out.println("Processing notification: " + message);

        Notification notification = new Notification();
        notification.setTopic("notifications");
        notification.setMessage(message);
        notification.setDelivered(true);

        notificationRepository.save(notification);

        notificationWebSocketHandler.sendNotification(message);
    }
}
