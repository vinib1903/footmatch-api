package com.teamcubation.footmatchapi.dto.response;

import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EstadioResponseDTO {

    private Long id;
    private String nome;
    private EnderecoResponseDTO endereco;
}
