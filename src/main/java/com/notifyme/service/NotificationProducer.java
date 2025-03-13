package com.notifyme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for producing notifications and sending them to a Kafka topic.
 */
@Service
@RequiredArgsConstructor
public class NotificationProducer {

    /**
     * KafkaTemplate for sending messages to Kafka topics.
     */
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Sends a notification message to the specified Kafka topic.
     *
     * @param topic   the name of the Kafka topic
     * @param message the notification message to be sent
     */
    public void sendNotification(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
