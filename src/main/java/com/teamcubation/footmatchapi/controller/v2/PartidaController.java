package com.teamcubation.footmatchapi.controller.v2;

import com.teamcubation.footmatchapi.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.service.kafka.PartidaServiceKafka;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("v2")
@RequestMapping("api/v2/partidas")
@RequiredArgsConstructor
public class PartidaController {

    private final PartidaServiceKafka partidaServiceKafka;

    @PostMapping
    public ResponseEntity<String> createPartida(@RequestBody @Valid PartidaRequestDTO dto) {
        partidaServiceKafka.enviarPartidaParaFilaCriacao(dto);
        return ResponseEntity.accepted().body("Partida enviada para processamento assíncrono (Kafka).");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePartida(@PathVariable Long id, @RequestBody @Valid PartidaRequestDTO dto) {

        partidaServiceKafka.enviarPartidaParaFilaAtualizacao(id, dto);
        return ResponseEntity.accepted().body("Partida enviada para processamento assíncrono (Kafka).");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePartida(@PathVariable Long id) {

        partidaServiceKafka.enviarPartidaParaFilaExclusao(id);
        return ResponseEntity.accepted().body("Partida enviada para processamento assíncrono (Kafka).");
    }
}
