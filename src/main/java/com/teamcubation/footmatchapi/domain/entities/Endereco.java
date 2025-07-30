package com.teamcubation.footmatchapi.domain.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Endereco {

    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    private String cep;
}
