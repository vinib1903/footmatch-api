package com.teamcubation.footmatchapi.controller.v2;

import com.teamcubation.footmatchapi.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.dto.response.PartidaResponseDTO;
import com.teamcubation.footmatchapi.service.PartidaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("v2")
@RequestMapping("api/v2/partidas")
@RequiredArgsConstructor
public class PartidaController {

    private final PartidaService partidaService;

    @PostMapping
    public ResponseEntity<String> createPartida(@RequestBody @Valid PartidaRequestDTO dto) {
        partidaService.enviarPartidaParaFila(dto);
        return ResponseEntity.accepted().body("Partida enviada para processamento assíncrono (Kafka).");
    }
}
