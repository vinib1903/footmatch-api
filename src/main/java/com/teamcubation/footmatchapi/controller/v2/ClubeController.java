package com.teamcubation.footmatchapi.controller.v2;

import com.teamcubation.footmatchapi.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.service.kafka.ClubeServiceKafka;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("clube-controller-v2")
@RequestMapping("api/v2/clubes")
@RequiredArgsConstructor
public class ClubeController {

    private final ClubeServiceKafka clubeServiceKafka;

    @PostMapping
    public ResponseEntity<String> createClube(@RequestBody @Valid ClubeRequestDTO dto) {

        clubeServiceKafka.enviarClubeParaFilaCriacao(dto);
        return ResponseEntity.accepted().body("Clube enviado para processamento assíncrono (Kafka).");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateClube(@PathVariable Long id, @RequestBody @Valid ClubeRequestDTO dto) {

        clubeServiceKafka.enviarClubeParaFilaAtualizacao(id, dto);
        return ResponseEntity.accepted().body("Ckube enviado para processamento assíncrono (Kafka).");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> disableClube(@PathVariable Long id) {

        clubeServiceKafka.enviarClubeParaFilaExclusao(id);
        return ResponseEntity.accepted().body("Clube enviado para processamento assíncrono (Kafka).");
    }
}
