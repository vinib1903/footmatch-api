package com.teamcubation.footmatchapi.service.email.strategy;

import com.teamcubation.footmatchapi.dto.request.EmailContentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("stadiumCreatedStrategy")
public class StadiumCreatedNotificationStrategy implements NotificationStrategy {

    @Value("${notification.email.to}")
    private String toEmail;

    @Override
    public boolean appliesTo(String message) {
        return message.startsWith("Novo estádio criado:");
    }

    @Override
    public EmailContentDTO createEmail(String message) {
        String subject = "Novo Estádio Criado";
        String body = String.format("Um novo estádio foi criado no sistema: %s", message);

        return new EmailContentDTO(toEmail, subject, body);
    }
}
