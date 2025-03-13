package com.notifyme.service;

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

    /**
     * ExecutorService for processing messages concurrently.
     */
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

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
    }
}
