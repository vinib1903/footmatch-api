package com.teamcubation.footmatchapi.adapters.inbound.kafka.consumer;

import com.teamcubation.footmatchapi.application.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.application.ports.out.NotificationPort;
import com.teamcubation.footmatchapi.application.usecase.EstadioUseCases;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY;

@Component
@RequiredArgsConstructor
@Slf4j
public class EstadioConsumer {

    private final EstadioUseCases estadioUseCases;
    private final NotificationPort notificationPort;

    @KafkaListener(
            topics = "estadios-criacao",
            groupId = "estadio-group",
            containerFactory = "estadioRequestKafkaListenerContainerFactory"
    )
    public void consumirEstadioCriacao(EstadioRequestDTO dto) {
        log.info("Consumindo mensagem para criar estádio: {}", dto);
        EstadioResponseDTO estadioCriado = estadioUseCases.criarEstadio(dto);
        notificationPort.sendNotification("Novo estádio criado: " + estadioCriado.getNome() + " - " + estadioCriado.getEndereco().getLocalidade() + "/" + estadioCriado.getEndereco().getUf());
        log.info("Estadio consumido e salvo com sucesso: {}", dto);
    }

    @KafkaListener(
            topics = "estadios-atualizacao",
            groupId = "estadio-group",
            containerFactory = "estadioRequestKafkaListenerContainerFactory"
    )
    public void consumirEstadioAtualizacao(@Header(RECEIVED_KEY) String id, EstadioRequestDTO dto) {
        log.info("Consumindo mensagem para atualizar estádio: {}", dto);
        estadioUseCases.atualizarEstadio(Long.valueOf(id), dto);
        log.info("Estadio consumido e atualizado com sucesso: {}", dto);
    }
}