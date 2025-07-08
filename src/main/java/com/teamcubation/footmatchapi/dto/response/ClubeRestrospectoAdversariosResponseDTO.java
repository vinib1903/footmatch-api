package com.teamcubation.footmatchapi.dto.response;

import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubeRestrospectoAdversariosResponseDTO {

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
