package com.teamcubation.footmatchapi.application.service.email;

import com.teamcubation.footmatchapi.application.usecase.EmailUseCases;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailUseCases {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (MailException ex) {
            log.error("Erro ao enviar email para: {}", to, ex);
            throw new RuntimeException("Erro ao enviar e-mail", ex);
        }
    }
}
