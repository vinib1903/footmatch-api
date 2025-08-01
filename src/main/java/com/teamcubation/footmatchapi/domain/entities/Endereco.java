package com.teamcubation.footmatchapi.domain.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Endereco {

    @Schema(description = "Logradouro do endereço.", example = "Rua Sarandi", required = true)
    private String logradouro;

    @Schema(description = "Bairro do endereço.", example = "Jardim Mauá", required = true)
    private String bairro;

    @Schema(description = "Cidade do endereço.", example = "Novo Hamburgo", required = true)
    private String localidade;

    @Schema(description = "UF do endereço.", example = "RS", required = true, allowableValues = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"})
    private String uf;

    @Schema(description = "CEP do endereço.", example = "93300-000", required = true)
    private String cep;
}
