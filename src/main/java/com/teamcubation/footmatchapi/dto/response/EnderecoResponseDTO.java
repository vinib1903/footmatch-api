package com.teamcubation.footmatchapi.dto.response;

import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EnderecoResponseDTO {

    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    private String cep;
}
