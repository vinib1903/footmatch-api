package com.teamcubation.footmatchapi.adapters.outbound.kafka.adapters;

import com.teamcubation.footmatchapi.application.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.adapters.outbound.kafka.producer.EstadioProducer;
import com.teamcubation.footmatchapi.application.ports.out.EstadioEventsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EstadioKafkaAdapter implements EstadioEventsPort {

    private final EstadioProducer estadioProducer;

    public void enviarEstadioParaFilaCriacao(EstadioRequestDTO dto) {

        estadioProducer.enviarEstadioCriacao(dto);
    }

    public void enviarEstadioParaFilaAtualizacao(Long id, EstadioRequestDTO dto) {

        estadioProducer.enviarEstadioAtualizacao(id, dto);
    }

    @Override
    public void notificarCriacaoEstadio(EstadioRequestDTO dto) {
        estadioProducer.enviarEstadioCriacao(dto);
    }

    @Override
    public void notificarAtualizacaoEstadio(Long id, EstadioRequestDTO dto) {
        estadioProducer.enviarEstadioAtualizacao(id, dto);
    }
}
