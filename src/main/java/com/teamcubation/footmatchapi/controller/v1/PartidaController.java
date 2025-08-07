package com.teamcubation.footmatchapi.controller.v1;

import com.teamcubation.footmatchapi.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.dto.response.ErroResponseDTO;
import com.teamcubation.footmatchapi.dto.response.PartidaResponseDTO;
import com.teamcubation.footmatchapi.service.partida.PartidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController("v1")
@RequestMapping("api/v1/partidas")
@Tag(name = "Partidas V1", description = "API para gerenciamento de partidas (Versão 1 - Parcialmente depreciada)")
@RequiredArgsConstructor
public class PartidaController {

    private final PartidaService partidaService;

    @GetMapping
    @Operation(summary = "Lista partidas com suporte a paginação e filtros",
            description = "Retorna uma lista paginada de partidas, permitindo filtrar por clube, estádio, goleada e papel, ordenando por data e hora.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de partidas retornada com sucesso",
                            content = @Content(schema = @Schema(implementation = PartidaResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos",
                            content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                            content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<Page<PartidaResponseDTO>> searchPartidas(
            @Parameter(description = "ID do clube para filtrar (opcional)") @RequestParam(required = false, name = "clubeId") Long clubeId,
            @Parameter(description = "ID do estádio para filtrar (opcional)") @RequestParam(required = false, name = "estadioId") Long estadioId,
            @Parameter(description = "Indica se a partida foi uma goleada (opcional)") @RequestParam(required = false) Boolean goleada,
            @Parameter(description = "Papel na partida (opcional)") @RequestParam(required = false) String papel,
            @Parameter(description = "Configuração da paginação (tamanho, ordenação, etc.)", example = "{\n" +
                    "  \"page\": 0,\n" +
                    "  \"size\": 10,\n" +
                    "  \"sort\": \"dataHora,desc\"\n" +
                    "}")
            @PageableDefault(size = 10, sort = "dataHora", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PartidaResponseDTO> page = partidaService.obterPartidas(clubeId, estadioId, goleada, papel, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retorna uma partida por ID",
            description = "Retorna um objeto PartidaResponseDTO baseado no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Partida encontrada",
                            content = @Content(schema = @Schema(implementation = PartidaResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Partida não encontrada",
                            content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                            content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<PartidaResponseDTO> getPartidaById(@Parameter(description = "ID da partida a ser buscada", example = "1") @PathVariable Long id) {
        PartidaResponseDTO partida = partidaService.obterPartidaPorId(id);
        return ResponseEntity.ok(partida);
    }

    @Deprecated
    @PostMapping
    @Operation(summary = "Cria uma nova partida (DEPRECATED)",
            description = "Endpoint desativado. Utilize a API V2 para criar partidas via Kafka.",
            deprecated = true)
    @ApiResponse(responseCode = "410", description = "Endpoint desativado", content = @Content)
    public ResponseEntity<PartidaResponseDTO> createPartida(
            @RequestBody PartidaRequestDTO dto) {

        throw new ResponseStatusException(
                HttpStatus.GONE,
                "Este endpoint está depreciado. Utilize a versão v2 da API."
        );
    }

    @Deprecated
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma partida (DEPRECATED)",
            description = "Endpoint desativado. Utilize a API V2 para atualizar partidas via Kafka.",
            deprecated = true)
    @ApiResponse(responseCode = "410", description = "Endpoint desativado", content = @Content)
    public ResponseEntity<PartidaResponseDTO> updatePartida(@PathVariable Long id, @RequestBody PartidaRequestDTO dto) {

        throw new ResponseStatusException(
                HttpStatus.GONE,
                "Este endpoint está depreciado. Utilize a versão v2 da API."
        );
    }

    @Deprecated
    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma partida (DEPRECATED)",
            description = "Endpoint desativado. Utilize a API V2 para excluir partidas via Kafka.",
            deprecated = true)
    @ApiResponse(responseCode = "410", description = "Endpoint desativado", content = @Content)
    public ResponseEntity<Void> deletePartida(@PathVariable Long id) {

        throw new ResponseStatusException(
                HttpStatus.GONE,
                "Este endpoint está depreciado. Utilize a versão v2 da API."
        );
    }
}