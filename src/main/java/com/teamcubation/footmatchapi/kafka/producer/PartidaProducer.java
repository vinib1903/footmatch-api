package com.teamcubation.footmatchapi.kafka.producer;

import com.teamcubation.footmatchapi.dto.request.PartidaRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartidaProducer {

    private final KafkaTemplate<String, PartidaRequestDTO> kafkaTemplate;

    public void enviarPartida(PartidaRequestDTO partida) {

        log.info("Enviando partida para Kafka: {}", partida);
        kafkaTemplate.send("partidas-topic", partida);
    }
}