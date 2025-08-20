package com.teamcubation.footmatchapi.adapters.inbound.controller.v1;

import com.teamcubation.footmatchapi.application.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.ErroResponseDTO;
import com.teamcubation.footmatchapi.application.service.clube.ClubeService;
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
@RequestMapping("/api/v1/clubes")
@Tag(name = "Clubes V1", description = "API para gerenciamento de clubes (Versão 1 - Parcialmente depreciada)")
@RequiredArgsConstructor
public class ClubeController {

    private final ClubeService clubeService;

    @GetMapping
    @Operation(summary = "Lista clubes com suporte a paginação e filtros",
            description = "Retorna uma lista paginada de clubes, permitindo filtrar por nome, estado e status.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clubes retornada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<Page<ClubeResponseDTO>> searchClubes(
            @Parameter(description = "Nome do clube para filtrar (opcional)") @RequestParam(required = false) String nome,
            @Parameter(description = "Sigla do estado para filtrar (opcional)") @RequestParam(required = false) String siglaEstado,
            @Parameter(description = "Status do clube (ativo/inativo) para filtrar (opcional)") @RequestParam(required = false) Boolean ativo,
            @Parameter(description = "Configuração da paginação (tamanho da página, ordenação)", example = "{\n" +
                    "  \"page\": 0,\n" +
                    "  \"size\": 10,\n" +
                    "  \"sort\": \"nome,desc\"\n" +
                    "}")
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<ClubeResponseDTO> page = clubeService.obterClubes(nome, siglaEstado, ativo, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retorna um clube por ID", description = "Retorna um objeto ClubeResponseDTO baseado no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Clube encontrado", content = @Content(schema = @Schema(implementation = ClubeResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Clube não encontrado", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<ClubeResponseDTO> getClubeById(@Parameter(description = "ID do clube a ser buscado", example = "1") @PathVariable Long id) {
        ClubeResponseDTO clube = clubeService.obterClubePorId(id);
        return ResponseEntity.ok(clube);
    }

    @Deprecated
    @PostMapping
    @Operation(summary = "Cria um novo clube (DEPRECATED)",
            description = "Endpoint desativado. Utilize a API V2 para criar clubes via Kafka.",
            deprecated = true)
    @ApiResponse(responseCode = "410", description = "Endpoint desativado", content = @Content)
    public ResponseEntity<ClubeResponseDTO> createClube(
            @RequestBody ClubeRequestDTO dto) {

        throw new ResponseStatusException(
                HttpStatus.GONE,
                "Este endpoint está depreciado. Utilize a versão v2 da API."
        );
    }

    @Deprecated
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um clube (DEPRECATED)",
            description = "Endpoint desativado. Utilize a API V2 para atualizar clubes via Kafka.",
            deprecated = true)
    @ApiResponse(responseCode = "410", description = "Endpoint desativado", content = @Content)
    public ResponseEntity<ClubeResponseDTO> updateClube(@PathVariable Long id, @RequestBody ClubeRequestDTO dto) {

        throw new ResponseStatusException(
                HttpStatus.GONE,
                "Este endpoint está depreciado. Utilize a versão v2 da API."
        );
    }

    @Deprecated
    @DeleteMapping("/{id}")
    @Operation(summary = "Desativa um clube (DEPRECATED)",
            description = "Endpoint desativado. Utilize a API V2 para desativar clubes via Kafka.",
            deprecated = true)
    @ApiResponse(responseCode = "410", description = "Endpoint desativado", content = @Content)
    public ResponseEntity<Void> disableClube(@PathVariable Long id) {

        throw new ResponseStatusException(
                HttpStatus.GONE,
                "Este endpoint está depreciado. Utilize a versão v2 da API."
        );
    }
}