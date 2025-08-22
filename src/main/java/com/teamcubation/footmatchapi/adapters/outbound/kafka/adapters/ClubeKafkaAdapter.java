package com.teamcubation.footmatchapi.adapters.outbound.kafka.adapters;

import com.teamcubation.footmatchapi.application.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.adapters.outbound.kafka.producer.ClubeProducer;
import com.teamcubation.footmatchapi.application.ports.out.ClubeEventsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClubeKafkaAdapter implements ClubeEventsPort {

    private final ClubeProducer clubeProducer;

    @Override
    public void notificarCriacaoClube(ClubeRequestDTO dto) {
        clubeProducer.enviarClubeCriacao(dto);
    }

    @Override
    public void notificarAtualizacaoClube(Long id, ClubeRequestDTO dto) {
        clubeProducer.enviarClubeAtualizacao(id, dto);
    }

    @Override
    public void notificarInativacaoClube(Long id) {
        clubeProducer.enviarClubeExclusao(id);
    }
}
