package com.teamcubation.footmatchapi.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ClubeResponseDTO {

    @Schema(description = "Identificador único do clube.", example = "1")
    private Long id;

    @Schema(description = "Nome do clube.", example = "Grêmio")
    private String nome;

    @Schema(description = "Sigla do estado do clube.", example = "RS", allowableValues = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"})
    private String siglaEstado;

    @Schema(description = "Data de criação do clube.", example = "1903-09-15", format = "yyyy-MM-dd")
    private LocalDate dataCriacao;

    @Schema(description = "Indica se o clube está ativo ou inativo.", example = "true")
    private Boolean ativo;
}
