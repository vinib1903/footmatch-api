package com.teamcubation.footmatchapi.controller;

import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.dto.response.PartidaResponseDTO;
import com.teamcubation.footmatchapi.service.PartidaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/partidas")
@RequiredArgsConstructor
public class PartidaController {

    private final PartidaService partidaService;

    @PostMapping
    public ResponseEntity<PartidaResponseDTO> createPartida(@RequestBody @Valid PartidaRequestDTO dto) {
        PartidaResponseDTO partida = partidaService.criarPartida(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(partida);
    }

    @GetMapping
    public ResponseEntity<Page<PartidaResponseDTO>> searchPartidas(
            @RequestParam(required = false) Long clubeId,
            @RequestParam(required = false) Long estadioId,
            @PageableDefault(size = 10, sort = "dataHora") Pageable pageable) {
        Page<PartidaResponseDTO> page = partidaService.obterPartidas(clubeId, estadioId, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaResponseDTO> getPartidaById(@PathVariable Long id) {
        PartidaResponseDTO partida = partidaService.obterPartidaPorId(id);
        return ResponseEntity.ok(partida);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidaResponseDTO> updatePartida(@PathVariable Long id, @RequestBody @Valid PartidaRequestDTO partidaRequestDTO) {
        PartidaResponseDTO partida = partidaService.atualizarPartida(id, partidaRequestDTO);
        return ResponseEntity.ok(partida);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartida(@PathVariable Long id) {
        partidaService.deletarPartida(id);
        return ResponseEntity.noContent().build();
    }
}
