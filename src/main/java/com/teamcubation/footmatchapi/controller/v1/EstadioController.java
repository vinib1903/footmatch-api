package com.teamcubation.footmatchapi.controller.v1;

import com.teamcubation.footmatchapi.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.dto.response.ErroResponseDTO;
import com.teamcubation.footmatchapi.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.service.estadio.EstadioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/v1/estadios")
@Tag(name = "Estádios V1", description = "API para gerenciamento de estádios (Versão 1 - Parcialmente depreciada)")
@RequiredArgsConstructor
public class EstadioController {

    private final EstadioService estadioService;

    @GetMapping
    @Operation(summary = "Lista estádios com suporte a paginação e filtro por nome",
            description = "Retorna uma lista paginada de estádios, permitindo filtrar por nome.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de estádios retornada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<Page<EstadioResponseDTO>> searchEstadios(
            @Parameter(description = "Nome do estádio para filtrar (opcional)") @RequestParam(required = false) String nome,
            @Parameter(description = "Configuração da paginação (tamanho da página, ordenação)", example = "{\n" +
                    "  \"page\": 0,\n" +
                    "  \"size\": 10,\n" +
                    "  \"sort\": \"nome,asc\"\n" +
                    "}")
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<EstadioResponseDTO> page = estadioService.obterEstadios(nome, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retorna um estádio por ID", description = "Retorna um objeto EstadioResponseDTO baseado no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estádio encontrado", content = @Content(schema = @Schema(implementation = EstadioResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Estádio não encontrado", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<EstadioResponseDTO> getEstadioById(@Parameter(description = "ID do estádio a ser buscado", example = "1") @PathVariable Long id) {
        EstadioResponseDTO estadio = estadioService.obterEstadioPorId(id);
        return ResponseEntity.ok(estadio);
    }

    @Deprecated
    @PostMapping
    @Operation(summary = "Cria um novo estádio (DEPRECATED)",
            description = "Endpoint desativado. Utilize a API V2 para criar estádios via Kafka.",
            deprecated = true)
    @ApiResponse(responseCode = "410", description = "Endpoint desativado", content = @Content)
    public ResponseEntity<EstadioResponseDTO> createEstadio(
            @RequestBody EstadioRequestDTO dto) {

        throw new ResponseStatusException(
                HttpStatus.GONE,
                "Este endpoint está depreciado. Utilize a versão v2 da API."
        );
    }

    @Deprecated
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um estádio (DEPRECATED)",
            description = "Endpoint desativado. Utilize a API V2 para atualizar estádios via Kafka.",
            deprecated = true)
    @ApiResponse(responseCode = "410", description = "Endpoint desativado", content = @Content)
    public ResponseEntity<EstadioResponseDTO> updateEstadio(@PathVariable Long id, @RequestBody EstadioRequestDTO dto) {

        throw new ResponseStatusException(
                HttpStatus.GONE,
                "Este endpoint está depreciado. Utilize a versão v2 da API."
        );
    }
}