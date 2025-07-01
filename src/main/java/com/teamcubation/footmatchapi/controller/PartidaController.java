package com.teamcubation.footmatchapi.controller;

import com.teamcubation.footmatchapi.domain.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.domain.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.domain.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.domain.dto.response.PartidaResponseDTO;
import com.teamcubation.footmatchapi.service.PartidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/partidas")
@RequiredArgsConstructor
public class PartidaController {

    private final PartidaService partidaService;

    @PostMapping
    public ResponseEntity<PartidaResponseDTO> createPartida() {
        PartidaResponseDTO partida = partidaService.criarPartida();
        return ResponseEntity.ok(partida);
    }

    @GetMapping
    public ResponseEntity<List<PartidaResponseDTO>> getAllPartidas() {
        List<PartidaResponseDTO> partidas = partidaService.obterPartidas();
        return ResponseEntity.ok(partidas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaResponseDTO> getPartidaById(@PathVariable Long id) {
        PartidaResponseDTO partida = partidaService.obterPartidaPorId(id);
        return ResponseEntity.ok(partida);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidaResponseDTO> updatePartida(@PathVariable Long id, @RequestBody PartidaRequestDTO partidaRequestDTO) {
        PartidaResponseDTO partida = partidaService.atualizarPartida(id, partidaRequestDTO);
        return ResponseEntity.ok(partida);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePartida(@PathVariable Long id) {
        partidaService.deletarPartida(id);
        return ResponseEntity.noContent().build();
    }
}
