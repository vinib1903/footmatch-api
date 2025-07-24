package com.teamcubation.footmatchapi.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EstadioResponseDTO {

    private Long id;
    private String nome;
    private EnderecoResponseDTO endereco;
}
