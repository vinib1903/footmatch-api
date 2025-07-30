package com.teamcubation.footmatchapi.dto.response;

import lombok.Data;

@Data
public class ViaCepResponseDTO {

    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    private String cep;
    private Boolean erro;
}
