package com.teamcubation.footmatchapi.controller;

import com.teamcubation.footmatchapi.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.service.EstadioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/estadios")
@RequiredArgsConstructor
public class EstadioController {

    private final EstadioService estadioService;

    @PostMapping
    public ResponseEntity<EstadioResponseDTO> createEstadio(@RequestBody @Valid EstadioRequestDTO estadioRequestDTO) {
        EstadioResponseDTO estadio = estadioService.criarEstadio(estadioRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(estadio);
    }

    @GetMapping
    public ResponseEntity<Page<EstadioResponseDTO>> listarEstadios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "asc") String order) {
        Page<EstadioResponseDTO> estadios = estadioService.obterEstadios(page, size, order);
        return ResponseEntity.ok(estadios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadioResponseDTO> getEstadioById(@PathVariable Long id) {
        EstadioResponseDTO estadio = estadioService.obterEstadioPorId(id);
        return ResponseEntity.ok(estadio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadioResponseDTO> updateEstadio(@PathVariable Long id, @RequestBody @Valid EstadioRequestDTO estadioRequestDTO) {
        EstadioResponseDTO estadio = estadioService.atualizarEstadio(id, estadioRequestDTO);
        return ResponseEntity.ok(estadio);
    }

    /*@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstadio(@PathVariable Long id) {
        estadioService.deletarEstadio(id);
        return ResponseEntity.noContent().build();
    }*/
}

