package com.teamcubation.footmatchapi.controller.v2;

import com.teamcubation.footmatchapi.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.service.kafka.EstadioServiceKafka;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("estadio-controller-v2")
@RequestMapping("api/v2/estadios")
@RequiredArgsConstructor
public class EstadioController {

    private final EstadioServiceKafka estadioServiceKafka;

    @PostMapping
    public ResponseEntity<String> createEstadio(@RequestBody @Valid EstadioRequestDTO dto) {

        estadioServiceKafka.enviarEstadioParaFilaCriacao(dto);
        return ResponseEntity.accepted().body("Estadio enviado para processamento assíncrono (Kafka).");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateEstadio(@PathVariable Long id, @RequestBody @Valid EstadioRequestDTO dto) {

        estadioServiceKafka.enviarEstadioParaFilaAtualizacao(id, dto);
        return ResponseEntity.accepted().body("Estadio enviado para processamento assíncrono (Kafka).");
    }
}
