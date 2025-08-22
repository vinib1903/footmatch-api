package com.teamcubation.footmatchapi.adapters.outbound.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@DependsOn("kafkaCommonConfig")
public class NotificationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public NotificationProducer(@Qualifier("stringKafkaTemplate") KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String notification) {
        kafkaTemplate.send("notifications", notification);
    }
}
