package com.teamcubation.footmatchapi.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ConfrontoDiretoResponseDTO {

    @Schema(description = "Informações do clube.")
    public ClubeRestrospectoAdversarioResponseDTO clube;

    @Schema(description = "Informações do adversário.")
    public ClubeRestrospectoAdversarioResponseDTO adversario;

    @Schema(description = "Listagem das partidas entre clube e adversário.")
    private List<PartidaResponseDTO> partidas;
}
