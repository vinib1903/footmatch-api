package com.teamcubation.footmatchapi.application.service.email;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> emailCaptor;

    private static final String TO_EMAIL = "test@example.com";
    private static final String SUBJECT = "Test Subject";
    private static final String BODY = "Test email body";

    @Test
    void sendEmail_ComDadosValidos_DeveEnviarEmail() {
        // Arrange
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        emailService.sendEmail(TO_EMAIL, SUBJECT, BODY);

        // Assert
        verify(mailSender, times(1)).send(emailCaptor.capture());
        
        SimpleMailMessage sentMessage = emailCaptor.getValue();
        assertNotNull(sentMessage);
        assertEquals(TO_EMAIL, sentMessage.getTo()[0]);
        assertEquals(SUBJECT, sentMessage.getSubject());
        assertEquals(BODY, sentMessage.getText());
    }

    @Test
    void sendEmail_ComFalhaNoEnvio_DeveLancarExcecao() {
        // Arrange
        String errorMessage = "Falha no envio de e-mail";
        doThrow(new MailException(errorMessage) {}).when(mailSender).send(any(SimpleMailMessage.class));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            emailService.sendEmail(TO_EMAIL, SUBJECT, BODY);
        });

        assertEquals("Erro ao enviar e-mail", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals(errorMessage, exception.getCause().getMessage());
        
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_ComVariosDestinatarios_DeveEnviarParaTodosJuntos() {
        // Arrange
        String[] recipients = {"test1@example.com", "test2@example.com"};
        String recipientsString = String.join(",", recipients);
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        emailService.sendEmail(recipientsString, SUBJECT, BODY);

        // Assert
        verify(mailSender, times(1)).send(emailCaptor.capture());
        
        SimpleMailMessage sentMessage = emailCaptor.getValue();
        assertNotNull(sentMessage);
        assertEquals(1, sentMessage.getTo().length);
        assertEquals(recipientsString, sentMessage.getTo()[0]); // Todos os destinatários como uma única string
        assertEquals(SUBJECT, sentMessage.getSubject());
        assertEquals(BODY, sentMessage.getText());
    }

    @Test
    void sendEmail_ComCorpoVazio_DeveEnviarEmailComCorpoVazio() {
        // Arrange
        String emptyBody = "";
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        emailService.sendEmail(TO_EMAIL, SUBJECT, emptyBody);

        // Assert
        verify(mailSender, times(1)).send(emailCaptor.capture());
        
        SimpleMailMessage sentMessage = emailCaptor.getValue();
        assertNotNull(sentMessage);
        assertEquals(TO_EMAIL, sentMessage.getTo()[0]);
        assertEquals(SUBJECT, sentMessage.getSubject());
        assertEquals(emptyBody, sentMessage.getText());
    }

    @Test
    void sendEmail_ComAssuntoVazio_DeveEnviarEmailComAssuntoVazio() {
        // Arrange
        String emptySubject = "";
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        emailService.sendEmail(TO_EMAIL, emptySubject, BODY);

        // Assert
        verify(mailSender, times(1)).send(emailCaptor.capture());
        
        SimpleMailMessage sentMessage = emailCaptor.getValue();
        assertNotNull(sentMessage);
        assertEquals(TO_EMAIL, sentMessage.getTo()[0]);
        assertEquals(emptySubject, sentMessage.getSubject());
        assertEquals(BODY, sentMessage.getText());
    }
}
