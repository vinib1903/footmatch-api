package com.teamcubation.footmatchapi.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EstadioResponseDTO {

    @Schema(description = "Identificador único do estadio.", example = "1")
    private Long id;

    @Schema(description = "Nome do estadio.", example = "Arena do Grêmio")
    private String nome;

    @Schema(description = "Endereço do estadio, gerado automaticamente no ato do cadastro.")
    private EnderecoResponseDTO endereco;
}
