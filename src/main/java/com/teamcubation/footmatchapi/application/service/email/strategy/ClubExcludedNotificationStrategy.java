package com.teamcubation.footmatchapi.application.service.email.strategy;

import com.teamcubation.footmatchapi.application.dto.request.EmailContentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("clubExcludedStrategy")
public class ClubExcludedNotificationStrategy implements NotificationStrategy {

    @Value("${notification.email.to}")
    private String toEmail;

    private static final String TRIGGER_PHRASE = "Clube com id ";

    @Override
    public boolean appliesTo(String message) {
        return message.startsWith(TRIGGER_PHRASE);
    }

    @Override
    public EmailContentDTO createEmail(String message) {
        String subject = "Clube excluido";
        String body = String.format("O clube correspondente à mensagem '%s' foi excluído do sistema.", message);

        return new EmailContentDTO(toEmail, subject, body);
    }
}
