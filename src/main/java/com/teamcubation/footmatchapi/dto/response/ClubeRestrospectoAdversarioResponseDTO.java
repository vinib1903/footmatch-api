package com.teamcubation.footmatchapi.dto.response;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubeRestrospectoAdversarioResponseDTO {

    //private Long adversarioId;
    private String adversarioNome;
    //private SiglaEstado adversarioSiglaEstado;
    private int partidas;
    private int vitorias;
    private int empates;
    private int derrotas;
    private int golsMarcados;
    private int golsSofridos;
}
