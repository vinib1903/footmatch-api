package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.domain.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.repository.ClubeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubeService {

    private final ClubeRepository clubeRepository;

    public ClubeResponseDTO criarClube(ClubeRequestDTO clubeRequestDTO) {
        return null;
    }

    public List<ClubeResponseDTO> obterClubes() {
        return null;
    }

    public ClubeResponseDTO obterClubePorId(Long id) {
        return null;
    }

    public ClubeResponseDTO atualizarClube(Long id, ClubeRequestDTO dto) {
        return null;
    }

    public void inativarClube(Long id) {
    }
}



