package com.teamcubation.footmatchapi.service.kafka;

import com.teamcubation.footmatchapi.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.kafka.producer.PartidaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartidaServiceKafka {

    private final PartidaProducer partidaProducer;

    public void enviarPartidaParaFilaCriacao(PartidaRequestDTO dto) {

        partidaProducer.enviarPartidaCriacao(dto);
    }

    public void enviarPartidaParaFilaAtualizacao(Long id,PartidaRequestDTO dto) {

        partidaProducer.enviarPartidaAtualizacao(id, dto);
    }

    public  void enviarPartidaParaFilaExclusao(Long id) {

        partidaProducer.enviarPartidaExclusao(id);
    }
}
