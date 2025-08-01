package com.teamcubation.footmatchapi.kafka.consumer;

import com.teamcubation.footmatchapi.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.service.PartidaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartidaConsumer {

    private final PartidaService partidaService;

    @KafkaListener(
            topics = "partidas-criacao",
            groupId = "partida-group",
            containerFactory = "partidaRequestKafkaListenerContainerFactory"
    )
    public void consumirPartidaCriacao(PartidaRequestDTO partidaDTO) {

        try {
            partidaService.criarPartida(partidaDTO);
            log.info("Partida consumida e salva com sucesso: {}", partidaDTO);
        } catch (ResponseStatusException ex) {

            log.warn("Erro ao salvar partida do Kafka (status={}): {}", ex.getStatusCode(), ex.getReason());
        } catch (IllegalArgumentException ex) {

            log.warn("Erro ao salvar partida do Kafka: {}", ex.getMessage());
        } catch (Exception ex) {

            log.error("Erro inesperado ao salvar partida do Kafka", ex);
        }
    }

    @KafkaListener(
            topics = "partidas-atualizacao",
            groupId = "partida-group",
            containerFactory = "partidaRequestKafkaListenerContainerFactory"
    )
    public void consumirPartidaAtualizacao(@Header(RECEIVED_KEY) String id, PartidaRequestDTO partidaDTO) {

        try {
            partidaService.atualizarPartida(Long.valueOf(id), partidaDTO);
            log.info("Partida consumida e atualizada com sucesso: {}", partidaDTO);
        } catch (ResponseStatusException ex) {

            log.warn("Erro ao atualizar partida do Kafka (status={}): {}", ex.getStatusCode(), ex.getReason());
        } catch (IllegalArgumentException ex) {

            log.warn("Erro ao atualizar partida do Kafka: {}", ex.getMessage());
        } catch (Exception ex) {

            log.error("Erro inesperado ao atualizar partida do Kafka", ex);
        }
    }

    @KafkaListener(
            topics = "partidas-exclusao",
            groupId = "partida-group",
            containerFactory = "stringKafkaListenerContainerFactory"
    )
    public void consumirPartidaExclusao(@Header(RECEIVED_KEY) String id) {

        try {
            partidaService.deletarPartida(Long.valueOf(id));
            log.info("Partida consumida e excluída com sucesso: {}", id);
        } catch (ResponseStatusException ex) {

            log.warn("Erro ao excluir partida do Kafka (status={}): {}", ex.getStatusCode(), ex.getReason());
        } catch (IllegalArgumentException ex) {

            log.warn("Erro ao excluir partida do Kafka: {}", ex.getMessage());
        } catch (Exception ex) {

            log.error("Erro inesperado ao excluir partida do Kafka", ex);
        }
    }
}