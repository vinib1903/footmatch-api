package com.teamcubation.footmatchapi.kafka.consumer;

import com.teamcubation.footmatchapi.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.service.EstadioService;
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
public class EstadioConsumer {

    private final EstadioService estadioService;

    @KafkaListener(
            topics = "estadios-criacao",
            groupId = "estadio-group",
            containerFactory = "estadioRequestKafkaListenerContainerFactory"
    )
    public void consumirEstadioCriacao(EstadioRequestDTO dto) {

        try {
            estadioService.criarEstadio(dto);
            log.info("Estadio consumido e salvo com sucesso: {}", dto);
        } catch (ResponseStatusException ex) {

            log.warn("Erro ao salvar estadio do Kafka (status={}): {}", ex.getStatusCode(), ex.getReason());
        } catch (IllegalArgumentException ex) {

            log.warn("Erro ao salvar estadio do Kafka: {}", ex.getMessage());
        } catch (Exception ex) {

            log.error("Erro inesperado ao salvar estadio do Kafka", ex);
        }
    }

    @KafkaListener(
            topics = "estadios-atualizacao",
            groupId = "estadio-group",
            containerFactory = "estadioRequestKafkaListenerContainerFactory"
    )
    public void consumirEstadioAtualizacao(@Header(RECEIVED_KEY) String id, EstadioRequestDTO dto) {

        try {
            estadioService.atualizarEstadio(Long.valueOf(id), dto);
            log.info("Estadio consumido e atualizado com sucesso: {}", dto);
        } catch (ResponseStatusException ex) {

            log.warn("Erro ao atualizar estadio do Kafka (status={}): {}", ex.getStatusCode(), ex.getReason());
        } catch (IllegalArgumentException ex) {

            log.warn("Erro ao atualizar estadio do Kafka: {}", ex.getMessage());
        } catch (Exception ex) {

            log.error("Erro inesperado ao atualizar estadio do Kafka", ex);
        }
    }
}