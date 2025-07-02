package com.teamcubation.footmatchapi.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EstadioRequestDTO {

    @NotBlank(message = "O nome do estádio é obrigatório.")
    @Size(min = 3, message = "O nome deve conter ao menos 3 caracteres.")
    private String nome;
}
