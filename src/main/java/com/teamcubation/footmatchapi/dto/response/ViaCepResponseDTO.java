package com.teamcubation.footmatchapi.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViaCepResponseDTO {

    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    private String cep;
    private Boolean erro;
}
