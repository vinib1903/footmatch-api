package com.teamcubation.footmatchapi.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ClubeRequestDTO {

    @NotBlank(message = "O nome do clube é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome do clube deve ter entre 2 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "A sigla do estado é obrigatória.")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Estado deve conter duas letras maiúsculas")
    private String siglaEstado;

    @NotNull(message = "A data de criação é obrigatória.")
    @PastOrPresent(message = "A data de criação não pode ser futura.")
    private LocalDate dataCriacao;

    @NotNull(message = "O status do clube é obrigatório.")
    private Boolean ativo;

}
