package com.teamcubation.footmatchapi.application.service.kafka;

import com.teamcubation.footmatchapi.application.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.adapters.outbound.kafka.producer.EstadioProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstadioServiceKafka {

    private final EstadioProducer estadioProducer;

    public void enviarEstadioParaFilaCriacao(EstadioRequestDTO dto) {

        estadioProducer.enviarEstadioCriacao(dto);
    }

    public void enviarEstadioParaFilaAtualizacao(Long id, EstadioRequestDTO dto) {

        estadioProducer.enviarEstadioAtualizacao(id, dto);
    }
}
