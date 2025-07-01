package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.domain.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.repository.EstadioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadioService {

    public final EstadioRepository estadioRepository;

    public EstadioResponseDTO criarEstadio(EstadioRequestDTO estadioRequestDTO) {
        return null;
    }

    public List<EstadioResponseDTO> obterEstadios() {
        return null;
    }

    public EstadioResponseDTO obterEstadioPorId(Long id) {
        return null;
    }

    public EstadioResponseDTO atualizarEstadio(Long id, EstadioRequestDTO estadioRequestDTO) {
        return null;
    }

    public void deletarEstadio(Long id) {
    }
}
