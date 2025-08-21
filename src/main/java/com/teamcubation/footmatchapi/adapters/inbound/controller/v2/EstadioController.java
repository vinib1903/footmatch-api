package com.teamcubation.footmatchapi.adapters.inbound.controller.v2;

import com.teamcubation.footmatchapi.application.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.ErroResponseDTO;
import com.teamcubation.footmatchapi.application.usecase.EstadioUseCases;
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

@RestController("estadio-controller-v2")
@RequestMapping("api/v2/estadios")
@Tag(name = "Estádios V2", description = "API para gerenciamento de estádios (Versão 2)")
@RequiredArgsConstructor
public class EstadioController {

    private final EstadioUseCases estadioUseCases;

    @PostMapping
    @Operation(summary = "Cria um novo estádio (Kafka)",
            description = "Envia uma solicitação para criar um novo estádio para a fila do Kafka.",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Solicitação de criação de estádio enviada para o Kafka", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(type = "string", example = "Estadio enviado para processamento assíncrono (Kafka)."))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<String> createEstadio(@RequestBody @Valid EstadioRequestDTO dto) {

        estadioUseCases.solicitarCriacaoEstadio(dto);
        return ResponseEntity.accepted().body("Estadio enviado para processamento assíncrono (Kafka).");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um estádio (Kafka)",
            description = "Envia uma solicitação para atualizar um estádio existente para a fila do Kafka.",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Solicitação de atualização de estádio enviada para o Kafka", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(type = "string", example = "Estadio enviado para processamento assíncrono (Kafka)."))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Estádio não encontrado", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<String> updateEstadio(
            @Parameter(description = "ID do estádio a ser atualizado", example = "1") @PathVariable Long id,
            @RequestBody @Valid EstadioRequestDTO dto) {

        estadioUseCases.solicitarAtualizacaoEstadio(id, dto);
        return ResponseEntity.accepted().body("Estadio enviado para processamento assíncrono (Kafka).");
    }
}