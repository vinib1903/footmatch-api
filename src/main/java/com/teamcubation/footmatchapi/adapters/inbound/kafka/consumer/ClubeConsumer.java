package com.teamcubation.footmatchapi.adapters.inbound.kafka.consumer;

import com.teamcubation.footmatchapi.application.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.application.ports.out.NotificationPort;
import com.teamcubation.footmatchapi.application.usecase.ClubeUseCases;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClubeConsumer {

    private final ClubeUseCases clubeUseCases;
    private final NotificationPort notificationPort;

    @KafkaListener(
            topics = "clubes-criacao",
            groupId = "clube-group",
            containerFactory = "clubeRequestKafkaListenerContainerFactory"
    )
    public void consumirClubeCriacao(ClubeRequestDTO dto) {
        log.info("Consumindo mensagem para criar clube: {}", dto);
        clubeUseCases.criarClube(dto);
        log.info("Clube consumido e salvo com sucesso: {}", dto);
        notificationPort.sendNotification("Novo clube criado: " + dto.toString());
    }

    @KafkaListener(
            topics = "clubes-atualizacao",
            groupId = "clube-group",
            containerFactory = "clubeRequestKafkaListenerContainerFactory"
    )
    public void consumirClubeAtualizacao(@Header(RECEIVED_KEY) String id, ClubeRequestDTO dto) {
        log.info("Consumindo mensagem para atualizar clube com id {}: {}", id, dto);
        clubeUseCases.atualizarClube(Long.valueOf(id), dto);
        log.info("Clube consumido e atualizado com sucesso: {}", dto);
        notificationPort.sendNotification("Clube com id " + id + " atualizado: " + dto.toString());
    }

    @KafkaListener(
            topics = "clubes-exclusao",
            groupId = "clube-group",
            containerFactory = "stringKafkaListenerContainerFactory"
    )
    public void consumirClubeExclusao(@Header(RECEIVED_KEY) String id) {
        log.info("Consumindo mensagem para excluir clube com id: {}", id);
        clubeUseCases.inativarClube(Long.valueOf(id));
        log.info("Clube consumido e excluído com sucesso: {}", id);
        notificationPort.sendNotification("Clube com id " + id + " excluído");
    }

    @DltHandler
    public void dltHandler(String message) {
        log.error("Mensagem recebida na DLT de clube: {}", message);
    }
}