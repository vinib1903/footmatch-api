package com.teamcubation.footmatchapi.adapters.inbound.controller.v1;

import com.teamcubation.footmatchapi.application.dto.response.*;
import com.teamcubation.footmatchapi.application.service.retrospecto.RetrospectoService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/retrospectos")
@Tag(name = "Retrospectos V1", description = "API para consulta de retrospectos e rankings de clubes (Versão 1)")
@RequiredArgsConstructor
public class RetrospectoController {

    private final RetrospectoService retrospectoService;

    @GetMapping("/{id}")
    @Operation(summary = "Retorna o retrospecto geral de um clube",
            description = "Retorna estatísticas gerais de um clube, como número de vitórias, derrotas, empates, etc.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retrospecto retornado com sucesso",
                            content = @Content(schema = @Schema(implementation = ClubeRetrospectoResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Clube não encontrado",
                            content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                            content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<ClubeRetrospectoResponseDTO> getClubeRetrospect(
            @Parameter(description = "ID do clube para buscar o retrospecto", example = "1") @PathVariable Long id,
            @Parameter(description = "Papel do clube (ex: mandante, visitante, ambos)", example = "ambos", required = false) @RequestParam(required = false) String papel) {

        ClubeRetrospectoResponseDTO clubeRestrospecto = retrospectoService.obterRetrospecto(id, papel);
        return ResponseEntity.ok(clubeRestrospecto);
    }

    @GetMapping("/{id}/contra-adversarios")
    @Operation(summary = "Retorna o retrospecto de um clube contra adversários específicos",
            description = "Retorna estatísticas detalhadas do clube contra cada adversário, com suporte a paginação.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retrospecto contra adversários retornado com sucesso",
                            content = @Content(schema = @Schema(implementation = ClubeRestrospectoAdversarioResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos",
                            content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Clube não encontrado",
                            content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                            content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<Page<ClubeRestrospectoAdversarioResponseDTO>> getClubeRetrospectVersusAdversarys(
            @Parameter(description = "ID do clube para buscar o retrospecto", example = "1") @PathVariable Long id,
            @Parameter(description = "Papel do clube (ex: mandante, visitante, ambos)", example = "ambos", required = false) @RequestParam(required = false) String papel,
            @Parameter(description = "Parâmetros de paginação (tamanho da página, ordenação, etc.)") @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<ClubeRestrospectoAdversarioResponseDTO> page = retrospectoService.obterRestrospectoAdversarios(id, papel, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{clubeId}/confrontos-diretos/{adversarioId}")
    @Operation(summary = "Retorna o histórico de confrontos diretos entre dois clubes",
            description = "Retorna estatísticas detalhadas dos confrontos diretos entre dois clubes, com opção de filtrar por papel.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Confronto direto retornado com sucesso",
                            content = @Content(schema = @Schema(implementation = ConfrontoDiretoResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Clube ou adversário não encontrado",
                            content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                            content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<ConfrontoDiretoResponseDTO> getConfrontosDiretos(
            @Parameter(description = "ID do primeiro clube", example = "1") @PathVariable Long clubeId,
            @Parameter(description = "ID do clube adversário", example = "2") @PathVariable Long adversarioId,
            @Parameter(description = "Papel do clube (ex: mandante, visitante, ambos)", example = "ambos", required = false) @RequestParam(required = false) String papel) {

        ConfrontoDiretoResponseDTO result = retrospectoService.obterConfrontoDireto(clubeId, adversarioId, papel);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/ranking")
    @Operation(summary = "Retorna o ranking dos clubes",
            description = "Retorna uma lista paginada dos clubes ordenados por um critério específico (ex: pontos, vitórias).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ranking retornado com sucesso",
                            content = @Content(schema = @Schema(implementation = RankingResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Critério de ordenação inválido",
                            content = @Content(schema = @Schema(implementation = ErroResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                            content = @Content(schema = @Schema(implementation = ErroResponseDTO.class)))
            })
    public ResponseEntity<Page<RankingResponseDTO>> getRanking(
            @Parameter(description = "Critério de ordenação (ex: pontos, vitorias)", example = "pontos", required = false) @RequestParam(defaultValue = "pontos") String criterio,
            @Parameter(description = "Parâmetros de paginação (tamanho da página, ordenação, etc.)", example = "{\n" +
                    "  \"page\": 0,\n" +
                    "  \"size\": 10,\n" +
                    "  \"sort\": \"pontos,desc\"\n" +
                    "}")
            @PageableDefault(size = 10) Pageable pageable) {

        Page<RankingResponseDTO> page = retrospectoService.obterRanking(criterio, pageable);
        return ResponseEntity.ok(page);
    }
}