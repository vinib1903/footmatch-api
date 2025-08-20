package com.teamcubation.footmatchapi.adapters.inbound.kafka.consumer;

import com.teamcubation.footmatchapi.application.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.application.service.clube.ClubeService;
import com.teamcubation.footmatchapi.application.service.kafka.NotificationServiceKafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClubeConsumer {

    private final ClubeService clubeService;
    private final NotificationServiceKafka notificationServiceKafka;

    @KafkaListener(
            topics = "clubes-criacao",
            groupId = "clube-group",
            containerFactory = "clubeRequestKafkaListenerContainerFactory"
    )
    public void consumirClubeCriacao(ClubeRequestDTO dto) {
        log.info("Consumindo mensagem para criar clube: {}", dto);
        clubeService.criarClube(dto);
        log.info("Clube consumido e salvo com sucesso: {}", dto);
        notificationServiceKafka.sendNotification("Novo clube criado: " + dto.toString());
    }

    @KafkaListener(
            topics = "clubes-atualizacao",
            groupId = "clube-group",
            containerFactory = "clubeRequestKafkaListenerContainerFactory"
    )
    public void consumirClubeAtualizacao(@Header(RECEIVED_KEY) String id, ClubeRequestDTO dto) {
        log.info("Consumindo mensagem para atualizar clube com id {}: {}", id, dto);
        clubeService.atualizarClube(Long.valueOf(id), dto);
        log.info("Clube consumido e atualizado com sucesso: {}", dto);
        notificationServiceKafka.sendNotification("Clube com id " + id + " atualizado: " + dto.toString());
    }

    @KafkaListener(
            topics = "clubes-exclusao",
            groupId = "clube-group",
            containerFactory = "stringKafkaListenerContainerFactory"
    )
    public void consumirClubeExclusao(@Header(RECEIVED_KEY) String id) {
        log.info("Consumindo mensagem para excluir clube com id: {}", id);
        clubeService.inativarClube(Long.valueOf(id));
        log.info("Clube consumido e excluído com sucesso: {}", id);
        notificationServiceKafka.sendNotification("Clube com id " + id + " excluído");
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