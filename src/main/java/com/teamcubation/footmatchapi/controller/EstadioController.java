package com.teamcubation.footmatchapi.controller;

import com.teamcubation.footmatchapi.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.service.EstadioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/estadios")
@RequiredArgsConstructor
public class EstadioController {

    private final EstadioService estadioService;

    @PostMapping
    public ResponseEntity<EstadioResponseDTO> createEstadio(
            @RequestBody @Valid EstadioRequestDTO dto) {

        EstadioResponseDTO estadio = estadioService.criarEstadio(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(estadio);
    }

    @GetMapping
    public ResponseEntity<Page<EstadioResponseDTO>> searchEstadios(
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<EstadioResponseDTO> page = estadioService.obterEstadios(nome,pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadioResponseDTO> getEstadioById(@PathVariable Long id) {

        EstadioResponseDTO estadio = estadioService.obterEstadioPorId(id);
        return ResponseEntity.ok(estadio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadioResponseDTO> updateEstadio(@PathVariable Long id, @RequestBody @Valid EstadioRequestDTO dto) {

        EstadioResponseDTO estadio = estadioService.atualizarEstadio(id, dto);
        return ResponseEntity.ok(estadio);
    }
}

