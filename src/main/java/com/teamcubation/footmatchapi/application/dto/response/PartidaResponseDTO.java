package com.teamcubation.footmatchapi.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PartidaResponseDTO {

    @Schema(description = "Identificador único da partida.", example = "1")
    private Long id;

    @Schema(description = "Dados do clube mandante da partida.")
    private ClubeResponseDTO mandante;

    @Schema(description = "Dados do clube visitante da partida.")
    private ClubeResponseDTO visitante;

    @Schema(description = "Dados do estádio da partida.")
    private EstadioResponseDTO estadio;

    @Schema(description = "Data e hora da partida.", example = "2023-01-01T00:00:00")
    private LocalDateTime dataHora;

    @Schema(description = "Número de gols do clube mandante.", example = "3")
    private Integer golsMandante;

    @Schema(description = "Número de gols do clube visitante.", example = "0")
    private Integer golsVisitante;
}
