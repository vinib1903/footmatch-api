package com.teamcubation.footmatchapi.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ViaCepResponseDTO {


    @Schema(description = "Logradouro do endereço.", example = "Rua Sarandi")
    private String logradouro;

    @Schema(description = "Bairro do endereço.", example = "Jardim Mauá")
    private String bairro;

    @Schema(description = "Cidade do endereço.", example = "Novo Hamburgo")
    private String localidade;

    @Schema(description = "Estado do endereço.", example = "Rio Grande do Sul")
    private String uf;

    @Schema(description = "CEP do endereço.", example = "93300-000")
    private String cep;

    @Schema(description = "Possível erro na consulta.", example = "false")
    private Boolean erro;
}
