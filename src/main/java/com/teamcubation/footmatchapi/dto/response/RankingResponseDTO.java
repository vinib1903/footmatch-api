package com.teamcubation.footmatchapi.dto.response;

import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RankingResponseDTO {

    private String nome;
    private Integer pontos;
    private Integer vitorias;
    private Integer empates;
    private Integer golsMarcados;
    private Integer partidas;
}
