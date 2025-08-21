package com.teamcubation.footmatchapi.application.usecase;

import com.teamcubation.footmatchapi.application.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.PartidaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PartidaUseCases {

    PartidaResponseDTO criarPartida(PartidaRequestDTO dto);
    Page<PartidaResponseDTO> obterPartidas(Long clubeId, Long estadioId, Boolean goleada, String papel, Pageable pageable);
    PartidaResponseDTO obterPartidaPorId(Long id);
    PartidaResponseDTO atualizarPartida(Long id, PartidaRequestDTO dto);
    void deletarPartida(Long id);
}
