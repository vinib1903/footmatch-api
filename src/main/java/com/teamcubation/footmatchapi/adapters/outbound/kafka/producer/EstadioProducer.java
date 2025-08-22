package com.teamcubation.footmatchapi.adapters.outbound.kafka.producer;

import com.teamcubation.footmatchapi.application.dto.request.EstadioRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstadioProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void enviarEstadioCriacao(EstadioRequestDTO dto) {

        log.info("Enviando estadio para Kafka: {}", dto);
        kafkaTemplate.send("estadios-criacao", dto);
    }

    public void enviarEstadioAtualizacao(Long id, EstadioRequestDTO dto) {

        log.info("Enviando estadio com id {} para Kafka: {}", id, dto);
        kafkaTemplate.send("estadios-atualizacao", String.valueOf(id), dto);
    }
}