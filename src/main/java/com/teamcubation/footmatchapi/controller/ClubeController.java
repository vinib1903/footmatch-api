package com.teamcubation.footmatchapi.controller;

import com.teamcubation.footmatchapi.domain.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.domain.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.service.ClubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/clubes")
@RequiredArgsConstructor
public class ClubeController {

    private final ClubeService clubeService;

    @PostMapping
    public ResponseEntity<ClubeResponseDTO> createClube(@RequestBody ClubeRequestDTO clubeRequestDTO) {
        ClubeResponseDTO clube = clubeService.criarClube(clubeRequestDTO);
        return ResponseEntity.ok(clube);
    }

    @GetMapping
    public ResponseEntity<List<ClubeResponseDTO>> getAllClubes() {
        List<ClubeResponseDTO> clubes = clubeService.obterClubes();
        return ResponseEntity.ok(clubes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubeResponseDTO> getClubeById(@PathVariable Long id) {
        ClubeResponseDTO clube = clubeService.obterClubePorId(id);
        return ResponseEntity.ok(clube);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClubeResponseDTO> updateClube(@PathVariable Long id, @RequestBody ClubeRequestDTO clubeRequestDTO) {
        ClubeResponseDTO clube = clubeService.atualizarClube(id, clubeRequestDTO);
        return ResponseEntity.ok(clube);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> disableClube(@PathVariable Long id) {
        clubeService.inativarClube(id);
        return ResponseEntity.noContent().build();
    }
}
