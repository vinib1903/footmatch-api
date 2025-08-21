package com.teamcubation.footmatchapi.application.usecase;

import com.teamcubation.footmatchapi.application.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.domain.entities.Estadio;

public interface EstadioUseCases {

    EstadioResponseDTO criarEstadio(EstadioRequestDTO estadioRequestDTO);
    EstadioResponseDTO obterEstadioPorId(Long id);
    EstadioResponseDTO atualizarEstadio(Long id, EstadioRequestDTO estadioRequestDTO);
    Estadio validarExistenciaEstadio(Long id);
}
