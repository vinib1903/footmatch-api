package com.teamcubation.footmatchapi.dto.response;

import lombok.*;
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ClubeRestrospectoAdversarioResponseDTO {

    private String adversarioNome;
    private int partidas;
    private int vitorias;
    private int empates;
    private int derrotas;
    private int golsMarcados;
    private int golsSofridos;
}
