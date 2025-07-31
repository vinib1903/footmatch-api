package com.teamcubation.footmatchapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ClubeRestrospectoAdversarioResponseDTO {

    @Schema(description = "Nome do adversário.", example = "Palmeiras")
    private String adversarioNome;

    @Schema(description = "Quantidade de partidas.", example = "3")
    private int partidas;

    @Schema(description = "Quantidade de vitorias.", example = "2")
    private int vitorias;

    @Schema(description = "Quantidade de empates.", example = "1")
    private int empates;

    @Schema(description = "Quantidade de derrotas.", example = "0")
    private int derrotas;

    @Schema(description = "Quantidade de gols marcados.", example = "8")
    private int golsMarcados;

    @Schema(description = "Quantidade de gols sofridos.", example = "3")
    private int golsSofridos;
}
