package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.dto.response.PartidaResponseDTO;
import com.teamcubation.footmatchapi.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartidaService {

    private final PartidaRepository partidaRepository;

    public PartidaResponseDTO criarPartida() {
        return null;
    }

    public List<PartidaResponseDTO> obterPartidas() {
        return null;
    }

    public PartidaResponseDTO obterPartidaPorId(Long id) {
        return null;
    }

    public PartidaResponseDTO atualizarPartida(Long id, PartidaRequestDTO partidaRequestDTO) {
        return null;
    }

    public void deletarPartida(Long id) {
        Partida oartida = partidaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada."));
    }
}
