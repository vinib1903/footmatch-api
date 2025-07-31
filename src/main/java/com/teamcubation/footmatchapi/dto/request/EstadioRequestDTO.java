package com.teamcubation.footmatchapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EstadioRequestDTO {

    @NotBlank(message = "O nome do estádio é obrigatório.")
    @Size(min = 3, message = "O nome deve conter ao menos 3 caracteres.")
    @Schema(description = "Nome do estádio.", example = "Arena do Grêmio", required = true)
    private String nome;

    @NotBlank(message = "O CEP do estádio é obrigatório.")
    @Pattern(regexp = "\\d{8}", message = "O CEP deve conter 8 dígitos numéricos.")
    @Schema(description = "CEP do estádio.", example = "93300-000", required = true)
    private String cep;
}
