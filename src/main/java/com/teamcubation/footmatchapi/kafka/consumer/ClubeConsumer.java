package com.teamcubation.footmatchapi.kafka.consumer;

import com.teamcubation.footmatchapi.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.service.ClubeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClubeConsumer {

    private final ClubeService clubeService;

    @KafkaListener(
            topics = "clubes-criacao",
            groupId = "clube-group",
            containerFactory = "clubeRequestKafkaListenerContainerFactory"
    )
    public void consumirClubeCriacao(ClubeRequestDTO dto) {
        try {
            clubeService.criarClube(dto);
            log.info("Clube consumido e salvo com sucesso: {}", dto);
        } catch (ResponseStatusException ex) {
            log.warn("Erro ao salvar clube do Kafka (status={}): {}", ex.getStatusCode(), ex.getReason());
        } catch (IllegalArgumentException ex) {
            log.warn("Erro ao salvar clube do Kafka: {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Erro inesperado ao salvar clube do Kafka", ex);
        }
    }

    @KafkaListener(
            topics = "clubes-atualizacao",
            groupId = "clube-group",
            containerFactory = "clubeRequestKafkaListenerContainerFactory"
    )
    public void consumirClubeAtualizacao(
            @Header(org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY) String id,
            ClubeRequestDTO dto) {
        try {
            clubeService.atualizarClube(Long.valueOf(id), dto);
            log.info("Clube consumido e atualizado com sucesso: {}", dto);
        } catch (ResponseStatusException ex) {
            log.warn("Erro ao atualizar clube do Kafka (status={}): {}", ex.getStatusCode(), ex.getReason());
        } catch (IllegalArgumentException ex) {
            log.warn("Erro ao atualizar clube do Kafka: {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Erro inesperado ao clube partida do Kafka", ex);
        }
    }

    @KafkaListener(
            topics = "clubes-exclusao",
            groupId = "clube-group",
            containerFactory = "stringKafkaListenerContainerFactory"
    )
    public void consumirClubeExclusao(@Header(KafkaHeaders.RECEIVED_KEY) String id) {
        log.info("Consumidor de exclusão recebeu id: {}", id);
        try {
            clubeService.inativarClube(Long.valueOf(id));
            log.info("Clube consumido e excluído com sucesso: {}", id);
        } catch (ResponseStatusException ex) {
            log.warn("Erro ao excluir clube do Kafka (status={}): {}", ex.getStatusCode(), ex.getReason());
        } catch (IllegalArgumentException ex) {
            log.warn("Erro ao excluir clube do Kafka: {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Erro inesperado ao excluir clube do Kafka", ex);
        }
    }
}