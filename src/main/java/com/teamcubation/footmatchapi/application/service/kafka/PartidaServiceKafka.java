package com.teamcubation.footmatchapi.application.service.kafka;

import com.teamcubation.footmatchapi.application.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.adapters.outbound.kafka.producer.PartidaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartidaServiceKafka {

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
}
