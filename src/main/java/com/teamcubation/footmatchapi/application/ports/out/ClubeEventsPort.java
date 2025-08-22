package com.teamcubation.footmatchapi.application.ports.out;

import com.teamcubation.footmatchapi.application.dto.request.ClubeRequestDTO;

public interface ClubeEventsPort {
    void notificarCriacaoClube(ClubeRequestDTO dto);
    void notificarAtualizacaoClube(Long id, ClubeRequestDTO dto);
    void notificarInativacaoClube(Long id);
}
