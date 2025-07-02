package com.teamcubation.footmatchapi.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PartidaResponseDTO {

    private Long id;

    private ClubeResponseDTO mandante;

    private ClubeResponseDTO visitante;

    private EstadioResponseDTO estadio;

    private LocalDateTime dataHora;

    private Integer golsMandante;

    private Integer golsVisitante;
}
