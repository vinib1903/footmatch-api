package com.teamcubation.footmatchapi.kafka.consumer;

import com.teamcubation.footmatchapi.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.service.estadio.EstadioService;
import com.teamcubation.footmatchapi.service.kafka.NotificationServiceKafka;
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
public class EstadioConsumer {

    private final EstadioService estadioService;
    private final NotificationServiceKafka notificationServiceKafka;

    @KafkaListener(
            topics = "estadios-criacao",
            groupId = "estadio-group",
            containerFactory = "estadioRequestKafkaListenerContainerFactory"
    )
    public void consumirEstadioCriacao(EstadioRequestDTO dto) {
        log.info("Consumindo mensagem para criar estádio: {}", dto);
        EstadioResponseDTO estadioCriado = estadioService.criarEstadio(dto);
        notificationServiceKafka.sendNotification("Novo estádio criado: " + estadioCriado.getNome() + " - " + estadioCriado.getEndereco().getLocalidade() + "/" + estadioCriado.getEndereco().getUf());
        log.info("Estadio consumido e salvo com sucesso: {}", dto);
    }

    @KafkaListener(
            topics = "estadios-atualizacao",
            groupId = "estadio-group",
            containerFactory = "estadioRequestKafkaListenerContainerFactory"
    )
    public void consumirEstadioAtualizacao(@Header(RECEIVED_KEY) String id, EstadioRequestDTO dto) {
        log.info("Consumindo mensagem para atualizar estádio: {}", dto);
        estadioService.atualizarEstadio(Long.valueOf(id), dto);
        log.info("Estadio consumido e atualizado com sucesso: {}", dto);
    }
}