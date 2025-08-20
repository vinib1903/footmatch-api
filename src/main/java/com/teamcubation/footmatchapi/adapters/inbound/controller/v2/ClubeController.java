package com.teamcubation.footmatchapi.adapters.inbound.controller.v2;

import com.teamcubation.footmatchapi.application.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.ErroResponseDTO;
import com.teamcubation.footmatchapi.application.service.kafka.ClubeServiceKafka;
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

@RestController("clube-controller-v2")
@RequestMapping("api/v2/clubes")
@Tag(name = "Clubes V2", description = "API para gerenciamento de clubes (Versão 2)")
@RequiredArgsConstructor
public class ClubeController {

    private final ClubeServiceKafka clubeServiceKafka;

    @PostMapping
    @Operation(summary = "Cria um novo clube (Kafka)",
            description = "Envia uma solicitação para criar um novo clube para a fila do Kafka.",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Solicitação de criação de clube enviada para o Kafka", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(type = "string", example = "Clube enviado para processamento assíncrono (Kafka)."))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<String> createClube(@RequestBody @Valid ClubeRequestDTO dto) {

        clubeServiceKafka.enviarClubeParaFilaCriacao(dto);
        return ResponseEntity.accepted().body("Clube enviado para processamento assíncrono (Kafka).");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um clube (Kafka)",
            description = "Envia uma solicitação para atualizar um clube existente para a fila do Kafka.",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Solicitação de atualização de clube enviada para o Kafka", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(type = "string", example = "Clube enviado para processamento assíncrono (Kafka)."))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Clube não encontrado", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<String> updateClube(
            @Parameter(description = "ID do clube a ser atualizado", example = "1") @PathVariable Long id,
            @RequestBody @Valid ClubeRequestDTO dto) {

        clubeServiceKafka.enviarClubeParaFilaAtualizacao(id, dto);
        return ResponseEntity.accepted().body("Ckube enviado para processamento assíncrono (Kafka).");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativa um clube (Kafka)",
            description = "Envia uma solicitação para desativar um clube para a fila do Kafka.",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Solicitação de desativação de clube enviada para o Kafka", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(type = "string", example = "Clube enviado para processamento assíncrono (Kafka)."))),
                    @ApiResponse(responseCode = "404", description = "Clube não encontrado", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<String> disableClube(
            @Parameter(description = "ID do clube a ser desativado", example = "1") @PathVariable Long id) {

        clubeServiceKafka.enviarClubeParaFilaExclusao(id);
        return ResponseEntity.accepted().body("Clube enviado para processamento assíncrono (Kafka).");
    }
}