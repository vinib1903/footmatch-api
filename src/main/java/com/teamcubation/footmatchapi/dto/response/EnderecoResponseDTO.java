package com.teamcubation.footmatchapi.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EnderecoResponseDTO {

    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    private String cep;
}
