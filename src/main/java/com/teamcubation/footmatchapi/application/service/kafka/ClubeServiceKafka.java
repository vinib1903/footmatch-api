package com.teamcubation.footmatchapi.application.service.kafka;

import com.teamcubation.footmatchapi.application.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.adapters.outbound.kafka.producer.ClubeProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubeServiceKafka {

    private final ClubeProducer clubeProducer;

    public void enviarClubeParaFilaCriacao(ClubeRequestDTO dto) {

        clubeProducer.enviarClubeCriacao(dto);
    }

    public void enviarClubeParaFilaAtualizacao(Long id, ClubeRequestDTO dto) {

        clubeProducer.enviarClubeAtualizacao(id, dto);
    }

    public void enviarClubeParaFilaExclusao(Long id) {

        clubeProducer.enviarClubeExclusao(id);
    }
}
