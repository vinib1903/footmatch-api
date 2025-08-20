package com.teamcubation.footmatchapi.adapters.inbound.kafka.consumer;

import com.teamcubation.footmatchapi.application.dto.request.EmailContentDTO;
import com.teamcubation.footmatchapi.application.service.email.EmailService;
import com.teamcubation.footmatchapi.application.service.email.strategy.NotificationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final List<NotificationStrategy> notificationStrategies;
    private final EmailService emailService;

    @KafkaListener(topics = "notifications",
            groupId = "notification-group",
            containerFactory = "stringKafkaListenerContainerFactory")
    public void consumeNotification(String message) {

        notificationStrategies
                .stream()
                .filter(strategy -> strategy.appliesTo(message))
                .findFirst()
                .ifPresentOrElse(
                        strategy -> {
                            EmailContentDTO emailContent = strategy.createEmail(message);
                            emailService.sendEmail(
                                    emailContent.getTo(),
                                    emailContent.getSubject(),
                                    emailContent.getBody()
                            );
                        },
                        () -> log.warn("Nenhuma estratégia de notificação encontrada para a mensagem: {}", message)
                );
    }
}
