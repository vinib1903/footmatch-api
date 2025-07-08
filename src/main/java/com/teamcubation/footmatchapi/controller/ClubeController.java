package com.teamcubation.footmatchapi.controller;

import com.teamcubation.footmatchapi.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.dto.response.ClubeRetrospectoResponseDTO;
import com.teamcubation.footmatchapi.service.ClubeService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/clubes")
@RequiredArgsConstructor
public class ClubeController {
    private final ClubeService clubeService;

    @PostMapping
    public ResponseEntity<ClubeResponseDTO> createClube(@RequestBody @Valid ClubeRequestDTO dto) {
        ClubeResponseDTO clube = clubeService.criarClube(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clube);
    }

    @GetMapping
    public ResponseEntity<Page<ClubeResponseDTO>> searchClubes(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String siglaEstado,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        Page<ClubeResponseDTO> page = clubeService.obterClubes(nome, siglaEstado, ativo, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubeResponseDTO> getClubeById(@PathVariable Long id) {
        ClubeResponseDTO clube = clubeService.obterClubePorId(id);
        return ResponseEntity.ok(clube);
    }

    @GetMapping("/{id}/retrospecto")
    public ResponseEntity<ClubeRetrospectoResponseDTO> getClubeRetrospect (@PathVariable Long id) {
        ClubeRetrospectoResponseDTO clubeRestrospecto = clubeService.obterRetrospecto(id);
        return ResponseEntity.ok(clubeRestrospecto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClubeResponseDTO> updateClube(@PathVariable Long id, @RequestBody @Valid ClubeRequestDTO dto) {
        ClubeResponseDTO clube = clubeService.atualizarClube(id, dto);
        return ResponseEntity.ok(clube);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> disableClube(@PathVariable Long id) {
        clubeService.inativarClube(id);
        return ResponseEntity.noContent().build();
    }
}
