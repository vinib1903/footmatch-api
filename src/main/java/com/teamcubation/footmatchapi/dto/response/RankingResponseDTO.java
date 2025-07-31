package com.teamcubation.footmatchapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RankingResponseDTO {

    @Schema(description = "Nome do clube.", example = "Grêmio")
    private String nome;

    @Schema(description = "Quantidade de pontos do clube, calculados com base nas partidas jogadas.", example = "45")
    private Integer pontos;

    @Schema(description = "Quantidade de vitorias do clube, com base nas partidas jogadas.", example = "10")
    private Integer vitorias;

    @Schema(description = "Quantidade de empates do clube, com base nas partidas jogadas.", example = "5")
    private Integer empates;

    @Schema(description = "Quantidade de gols marcados pelo clube, com base nas partidas jogadas.", example = "20")
    private Integer golsMarcados;

    @Schema(description = "Quantidade de partidas jogadas pelo clube.", example = "10")
    private Integer partidas;
}
