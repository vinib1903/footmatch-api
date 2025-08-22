package com.teamcubation.footmatchapi.application.usecase;

import com.teamcubation.footmatchapi.application.dto.response.ClubeRestrospectoAdversarioResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.ClubeRetrospectoResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.ConfrontoDiretoResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.RankingResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RetrospectoUseCases {

    ClubeRetrospectoResponseDTO obterRetrospecto(Long id, String papel);
    Page<ClubeRestrospectoAdversarioResponseDTO> obterRestrospectoAdversarios(Long id, String papel, Pageable pageable);
    ConfrontoDiretoResponseDTO obterConfrontoDireto(Long clubeId, Long adversarioId, String papel);
    Page<RankingResponseDTO> obterRanking(String criterio, Pageable pageable);
}
