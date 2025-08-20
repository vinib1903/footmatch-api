package com.teamcubation.footmatchapi.adapters.inbound.kafka.consumer;

import com.teamcubation.footmatchapi.application.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.application.service.kafka.NotificationServiceKafka;
import com.teamcubation.footmatchapi.application.service.partida.PartidaService;
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
public class PartidaConsumer {

    private final PartidaService partidaService;
    private final NotificationServiceKafka notificationServiceKafka;

    @KafkaListener(
            topics = "partidas-criacao",
            groupId = "partida-group",
            containerFactory = "partidaRequestKafkaListenerContainerFactory"
    )
    public void consumirPartidaCriacao(PartidaRequestDTO partidaDTO) {
        partidaService.criarPartida(partidaDTO);
        log.info("Partida consumida e salva com sucesso: {}", partidaDTO);
    }

    @KafkaListener(
            topics = "partidas-atualizacao",
            groupId = "partida-group",
            containerFactory = "partidaRequestKafkaListenerContainerFactory"
    )
    public void consumirPartidaAtualizacao(@Header(RECEIVED_KEY) String id, PartidaRequestDTO partidaDTO) {
        partidaService.atualizarPartida(Long.valueOf(id), partidaDTO);
        log.info("Partida consumida e atualizada com sucesso: {}", partidaDTO);
    }

    @KafkaListener(
            topics = "partidas-exclusao",
            groupId = "partida-group",
            containerFactory = "stringKafkaListenerContainerFactory"
    )
    public void consumirPartidaExclusao(@Header(RECEIVED_KEY) String id) {
        partidaService.deletarPartida(Long.valueOf(id));
        log.info("Partida consumida e excluída com sucesso: {}", id);
    }

    @DltHandler
    public void dltHandler(String message, 
                           @Header(KafkaHeaders.DLT_ORIGINAL_TOPIC) String originalTopic, 
                           @Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String exceptionMessage) {
        
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