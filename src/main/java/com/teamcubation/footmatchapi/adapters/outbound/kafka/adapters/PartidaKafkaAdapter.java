package com.teamcubation.footmatchapi.adapters.outbound.kafka.adapters;

import com.teamcubation.footmatchapi.application.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.adapters.outbound.kafka.producer.PartidaProducer;
import com.teamcubation.footmatchapi.application.ports.out.PartidaEventsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartidaKafkaAdapter implements PartidaEventsPort {

    private final PartidaProducer partidaProducer;

    public void enviarPartidaParaFilaCriacao(PartidaRequestDTO dto) {

        partidaProducer.enviarPartidaCriacao(dto);
    }

    public void enviarPartidaParaFilaAtualizacao(Long id, PartidaRequestDTO dto) {

        partidaProducer.enviarPartidaAtualizacao(id, dto);
    }

    public void enviarPartidaParaFilaExclusao(Long id) {

        partidaProducer.enviarPartidaExclusao(id);
    }

    @Override
    public void notificarCriacaoPartida(PartidaRequestDTO dto) {
        partidaProducer.enviarPartidaCriacao(dto);
    }

    @Override
    public void notificarAtualizacaoPartida(Long id, PartidaRequestDTO dto) {
        partidaProducer.enviarPartidaAtualizacao(id, dto);
    }

    @Override
    public void notificarExclusaoPartida(Long id) {

    }
}
