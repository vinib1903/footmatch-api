package com.teamcubation.footmatchapi.controller;

import com.teamcubation.footmatchapi.dto.response.ClubeRestrospectoAdversarioResponseDTO;
import com.teamcubation.footmatchapi.dto.response.ClubeRetrospectoResponseDTO;
import com.teamcubation.footmatchapi.dto.response.ConfrontoDiretoResponseDTO;
import com.teamcubation.footmatchapi.dto.response.RankingResponseDTO;
import com.teamcubation.footmatchapi.service.RetrospectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/retrospectos")
@RequiredArgsConstructor
public class RetrospectoController {

    private final RetrospectoService retrospectoService;

    @GetMapping("/{id}")
    public ResponseEntity<ClubeRetrospectoResponseDTO> getClubeRetrospect(@PathVariable Long id) {
        ClubeRetrospectoResponseDTO clubeRestrospecto = retrospectoService.obterRetrospecto(id);
        return ResponseEntity.ok(clubeRestrospecto);
    }

    @GetMapping("/{id}/contra-adversarios")
    public ResponseEntity<Page<ClubeRestrospectoAdversarioResponseDTO>> getClubeRetrospectVersusAdversarys(@PathVariable Long id,
                                                                                                           @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        Page<ClubeRestrospectoAdversarioResponseDTO> page = retrospectoService.obterRestrospectoAdversarios(id, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{clubeId}/confrontos-diretos/{adversarioId}")
    public ResponseEntity<ConfrontoDiretoResponseDTO> getConfrontosDiretos(
            @PathVariable Long clubeId, @PathVariable Long adversarioId) {
        ConfrontoDiretoResponseDTO result = retrospectoService.obterConfrontoDireto(clubeId, adversarioId);
        return ResponseEntity.ok(result);
    }

    //ToDo: verificar pois nao esta retornando corretamnete todos os dados
    @GetMapping("/ranking")
    public ResponseEntity<Page<RankingResponseDTO>> getRanking(
            @RequestParam(defaultValue = "pontos") String criterio,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<RankingResponseDTO> page = retrospectoService.obterRanking(criterio, pageable);
        return ResponseEntity.ok(page);

    }
}
