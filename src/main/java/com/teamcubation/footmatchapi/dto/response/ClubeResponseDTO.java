package com.teamcubation.footmatchapi.dto.response;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ClubeResponseDTO {

    private Long id;
    private String nome;
    private String siglaEstado;
    private LocalDate dataCriacao;
    private Boolean ativo;
}
