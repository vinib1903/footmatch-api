package com.teamcubation.footmatchapi.kafka.consumer;

import com.teamcubation.footmatchapi.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.service.estadio.EstadioService;
import com.teamcubation.footmatchapi.service.kafka.NotificationServiceKafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstadioConsumer {

    private final EstadioService estadioService;
    private final NotificationServiceKafka notificationServiceKafka;

    @KafkaListener(
            topics = "estadios-criacao",
            groupId = "estadio-group",
            containerFactory = "estadioRequestKafkaListenerContainerFactory"
    )
    public void consumirEstadioCriacao(EstadioRequestDTO dto) {
        log.info("Consumindo mensagem para criar estádio: {}", dto);
        estadioService.criarEstadio(dto);
        log.info("Estadio consumido e salvo com sucesso: {}", dto);
    }

    @KafkaListener(
            topics = "estadios-atualizacao",
            groupId = "estadio-group",
            containerFactory = "estadioRequestKafkaListenerContainerFactory"
    )
    public void consumirEstadioAtualizacao(@Header(RECEIVED_KEY) String id, EstadioRequestDTO dto) {
        log.info("Consumindo mensagem para atualizar estádio: {}", dto);
        estadioService.atualizarEstadio(Long.valueOf(id), dto);
        log.info("Estadio consumido e atualizado com sucesso: {}", dto);
    }

    @DltHandler
    public void dltHandler(String message, @Header(KafkaHeaders.DLT_ORIGINAL_TOPIC) String originalTopic, @Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String exceptionMessage) {
        String cleanedReason = exceptionMessage;
        int colonIndex = exceptionMessage.indexOf(':');
        if (colonIndex != -1) {
            cleanedReason = exceptionMessage.substring(colonIndex + 1).trim();
        }

        String errorMessage = String.format(
                "Uma mensagem falhou em todas as tentativas de processamento e foi para a DLT.\n\n" +
                        "Tópico Original: %s\n\n" +
                        "Payload: %s\n\n" +
                        "Motivo da Falha: %s\n\n",
                originalTopic, message, cleanedReason
        );
        log.error("MENSAGEM NA DLT: {}\n", errorMessage);
    }
}