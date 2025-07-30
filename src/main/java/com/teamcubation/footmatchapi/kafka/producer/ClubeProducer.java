package com.teamcubation.footmatchapi.kafka.producer;

import com.teamcubation.footmatchapi.dto.request.ClubeRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClubeProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void enviarClubeCriacao(ClubeRequestDTO dto) {

        log.info("Enviando clube para Kafka: {}", dto);
        kafkaTemplate.send("clubes-criacao", dto);
    }

    public void enviarClubeAtualizacao(Long id, ClubeRequestDTO dto) {

        log.info("Enviando clube com id {} para Kafka: {}", id, dto);
        kafkaTemplate.send("clubes-atualizacao", String.valueOf(id), dto);
    }

    public  void enviarClubeExclusao(Long id) {

        log.info("Enviando clube com id {} para Kafka", id);
        kafkaTemplate.send("clubes-exclusao", String.valueOf(id), String.valueOf(id));
    }
}