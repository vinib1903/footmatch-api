package com.teamcubation.footmatchapi.service.kafka;

import com.teamcubation.footmatchapi.kafka.producer.NotificationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceKafka {

    private final NotificationProducer notificacaoProducer;

    public void sendNotification(String notificacao) {
        notificacaoProducer.send(notificacao);
    }
}
