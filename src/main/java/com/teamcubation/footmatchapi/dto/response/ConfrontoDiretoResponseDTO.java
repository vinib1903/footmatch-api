package com.teamcubation.footmatchapi.dto.response;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ConfrontoDiretoResponseDTO {

    public ClubeRestrospectoAdversarioResponseDTO clube;
    public ClubeRestrospectoAdversarioResponseDTO adversario;
    private List<PartidaResponseDTO> partidas;
}
