package com.teamcubation.footmatchapi.application.ports.out;

import com.teamcubation.footmatchapi.application.dto.request.PartidaRequestDTO;

public interface PartidaEventsPort {
    void notificarCriacaoPartida(PartidaRequestDTO dto);
    void notificarAtualizacaoPartida(Long id, PartidaRequestDTO dto);
    void notificarExclusaoPartida(Long id);
}
