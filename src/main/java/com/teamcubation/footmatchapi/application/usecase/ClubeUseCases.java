package com.teamcubation.footmatchapi.application.usecase;

import com.teamcubation.footmatchapi.application.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.domain.entities.Clube;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubeUseCases {

    ClubeResponseDTO criarClube(ClubeRequestDTO dto);

    Page<ClubeResponseDTO> obterClubes(String nome, String siglaEstado, Boolean ativo, Pageable pageable);

    ClubeResponseDTO obterClubePorId(Long id);

    ClubeResponseDTO atualizarClube(Long id, ClubeRequestDTO dto);

    Clube validarExistenciaClube(Long id);

    void inativarClube(Long id);
}
