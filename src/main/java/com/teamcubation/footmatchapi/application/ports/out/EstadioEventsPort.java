package com.teamcubation.footmatchapi.application.ports.out;

import com.teamcubation.footmatchapi.application.dto.request.EstadioRequestDTO;

public interface EstadioEventsPort {
    void notificarCriacaoEstadio(EstadioRequestDTO dto);
    void notificarAtualizacaoEstadio(Long id, EstadioRequestDTO dto);
}
