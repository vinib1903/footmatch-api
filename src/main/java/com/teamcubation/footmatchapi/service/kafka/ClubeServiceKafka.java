package com.teamcubation.footmatchapi.service.kafka;

import com.teamcubation.footmatchapi.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.kafka.producer.ClubeProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubeServiceKafka {

    private final ClubeProducer clubeProducer;

    public void enviarClubeParaFilaCriacao(ClubeRequestDTO dto) {

        clubeProducer.enviarClubeCriacao(dto);
    }

    public void enviarClubeParaFilaAtualizacao(Long id,ClubeRequestDTO dto) {

        clubeProducer.enviarClubeAtualizacao(id, dto);
    }

    public  void enviarClubeParaFilaExclusao(Long id) {

        clubeProducer.enviarClubeExclusao(id);
    }
}
