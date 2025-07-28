package com.teamcubation.footmatchapi.kafka.consumer;

import com.teamcubation.footmatchapi.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.service.PartidaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartidaConsumer {

    private final PartidaService partidaService;

    @KafkaListener(topics = "partidas-topic", groupId = "partida-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumirPartida(PartidaRequestDTO partidaDTO) {
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
}