package com.teamcubation.footmatchapi.adapters.inbound.controller.v2;

import com.teamcubation.footmatchapi.application.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.ErroResponseDTO;
import com.teamcubation.footmatchapi.application.service.kafka.PartidaServiceKafka;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("partida-controller-v2")
@RequestMapping("api/v2/partidas")
@Tag(name = "Partidas V2", description = "APIs para gerenciamento de partidas (Versão 2)")
@RequiredArgsConstructor
public class PartidaController {

    private final PartidaServiceKafka partidaServiceKafka;

    @PostMapping
    @Operation(summary = "Cria uma nova partida (Kafka)",
            description = "Envia uma solicitação para criar uma nova partida para a fila do Kafka.",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Solicitação de criação de partida enviada para o Kafka", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(type = "string", example = "Partida enviada para processamento assíncrono (Kafka)."))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<String> createPartida(@RequestBody @Valid PartidaRequestDTO dto) {

        partidaServiceKafka.enviarPartidaParaFilaCriacao(dto);
        return ResponseEntity.accepted().body("Partida enviada para processamento assíncrono (Kafka).");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma partida (Kafka)",
            description = "Envia uma solicitação para atualizar uma partida existente para a fila do Kafka.",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Solicitação de atualização de partida enviada para o Kafka", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(type = "string", example = "Partida enviada para processamento assíncrono (Kafka)."))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Partida não encontrada", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<String> updatePartida(
            @Parameter(description = "ID da partida a ser atualizada", example = "1") @PathVariable Long id,
            @RequestBody @Valid PartidaRequestDTO dto) {


        partidaServiceKafka.enviarPartidaParaFilaAtualizacao(id, dto);
        return ResponseEntity.accepted().body("Partida enviada para processamento assíncrono (Kafka).");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma partida (Kafka)",
            description = "Envia uma solicitação para excluir uma partida para a fila do Kafka.",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Solicitação de exclusão de partida enviada para o Kafka", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(type = "string", example = "Partida enviada para processamento assíncrono (Kafka)."))),
                    @ApiResponse(responseCode = "404", description = "Partida não encontrada", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<String> deletePartida(@Parameter(description = "ID da partida a ser excluída", example = "1") @PathVariable Long id) {


        partidaServiceKafka.enviarPartidaParaFilaExclusao(id);
        return ResponseEntity.accepted().body("Partida enviada para processamento assíncrono (Kafka).");
    }
}