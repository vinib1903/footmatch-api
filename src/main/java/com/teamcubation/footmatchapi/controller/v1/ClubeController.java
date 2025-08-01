package com.teamcubation.footmatchapi.controller.v1;

import com.teamcubation.footmatchapi.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.service.ClubeService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/clubes")
@RequiredArgsConstructor
public class ClubeController {

    private final ClubeService clubeService;

    @PostMapping
    public ResponseEntity<ClubeResponseDTO> createClube(
            @RequestBody @Valid ClubeRequestDTO dto) {

        throw new ResponseStatusException(
                HttpStatus.GONE,
                "Este endpoint está depreciado. Utilize a versão v2 da API."
        );
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

    @PutMapping("/{id}")
    public ResponseEntity<ClubeResponseDTO> updateClube(@PathVariable Long id, @RequestBody @Valid ClubeRequestDTO dto) {

        throw new ResponseStatusException(
                HttpStatus.GONE,
                "Este endpoint está depreciado. Utilize a versão v2 da API."
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> disableClube(@PathVariable Long id) {

        throw new ResponseStatusException(
                HttpStatus.GONE,
                "Este endpoint está depreciado. Utilize a versão v2 da API."
        );
    }
}
