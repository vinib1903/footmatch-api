package com.teamcubation.footmatchapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ClubeRetrospectoResponseDTO {

    @Schema(description = "Número de partidas jogadas pelo clube.", example = "10")
    private int partidas;

    @Schema(description = "Número de vitorias pelo clube.", example = "5")
    private int vitorias;

    @Schema(description = "Número de empates pelo clube.", example = "3")
    private int empates;

    @Schema(description = "Número de derrotas pelo clube.", example = "2")
    private int derrotas;

    @Schema(description = "Número de gols marcados pelo clube.", example = "15")
    private int golsMarcados;

    @Schema(description = "Número de gols sofridos pelo clube.", example = "10")
    private int golsSofridos;
}
