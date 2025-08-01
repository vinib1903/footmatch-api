package com.teamcubation.footmatchapi.controller.v1;

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
    public ResponseEntity<ClubeRetrospectoResponseDTO> getClubeRetrospect(@PathVariable Long id,
                                                                          @RequestParam(required = false) String papel) {

        ClubeRetrospectoResponseDTO clubeRestrospecto = retrospectoService.obterRetrospecto(id, papel);
        return ResponseEntity.ok(clubeRestrospecto);
    }

    @GetMapping("/{id}/contra-adversarios")
    public ResponseEntity<Page<ClubeRestrospectoAdversarioResponseDTO>> getClubeRetrospectVersusAdversarys(@PathVariable Long id,
                                                                                                           @RequestParam(required = false) String papel,
                                                                                                           @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<ClubeRestrospectoAdversarioResponseDTO> page = retrospectoService.obterRestrospectoAdversarios(id, papel, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{clubeId}/confrontos-diretos/{adversarioId}")
    public ResponseEntity<ConfrontoDiretoResponseDTO> getConfrontosDiretos(
            @PathVariable Long clubeId,
            @PathVariable Long adversarioId,
            @RequestParam(required = false) String papel) {

        ConfrontoDiretoResponseDTO result = retrospectoService.obterConfrontoDireto(clubeId, adversarioId, papel);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/ranking")
    public ResponseEntity<Page<RankingResponseDTO>> getRanking(
            @RequestParam(defaultValue = "pontos") String criterio,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<RankingResponseDTO> page = retrospectoService.obterRanking(criterio, pageable);
        return ResponseEntity.ok(page);
    }
}
