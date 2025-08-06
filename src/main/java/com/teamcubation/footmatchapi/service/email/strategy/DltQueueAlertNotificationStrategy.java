package com.teamcubation.footmatchapi.service.email.strategy;

import com.teamcubation.footmatchapi.dto.request.EmailContentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("dltAlertStrategy")
public class DltQueueAlertNotificationStrategy implements NotificationStrategy {

    @Value("${notification.email.to}")
    private String toEmail;

    private static final String TRIGGER_PHRASE = "Alerta:";

    @Override
    public boolean appliesTo(String message) {
        return message.contains(TRIGGER_PHRASE);
    }

    @Override
    public EmailContentDTO createEmail(String message) {
        String subject = "Alerta de Monitoramento do Kafka DLT";
        String body = message;

        return new EmailContentDTO(toEmail, subject, body);
    }
}
