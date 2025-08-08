package com.teamcubation.footmatchapi.service.email.strategy;

import com.teamcubation.footmatchapi.dto.request.EmailContentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component("dltAlertStrategy")
public class DltQueueAlertNotificationStrategy implements NotificationStrategy {

    @Value("${notification.email.to}")
    private String toEmail;

    private static final String DAILY_REPORT_TRIGGER = "Relatório Diário DLT:";

    @Override
    public boolean appliesTo(String message) {
        return message.contains(DAILY_REPORT_TRIGGER);
    }

    @Override
    public EmailContentDTO createEmail(String message) {
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String subject = String.format("📊 Relatório Diário DLT - %s", today);
        String body = message;

        return new EmailContentDTO(toEmail, subject, body);
    }
}