package com.teamcubation.footmatchapi.adapters.outbound.kafka.adapters;

import com.teamcubation.footmatchapi.adapters.outbound.kafka.producer.NotificationProducer;
import com.teamcubation.footmatchapi.application.ports.out.NotificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationKafkaAdapter implements NotificationPort {

    private final NotificationProducer notificacaoProducer;

    @Override
    public void sendNotification(String notificacao) {
        notificacaoProducer.send(notificacao);
    }
}
