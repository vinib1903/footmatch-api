package com.teamcubation.footmatchapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ClubeRequestDTO {

    @NotBlank(message = "O nome do clube é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome do clube deve ter entre 2 e 100 caracteres.")
    @Schema(description = "Nome do clube.", example = "Grêmio", required = true)
    private String nome;

    @NotBlank(message = "A sigla do estado é obrigatória.")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Estado deve conter duas letras maiúsculas")
    @Schema(description = "Sigla do estado do clube.", example = "RS", required = true)
    private String siglaEstado;

    @NotNull(message = "A data de criação é obrigatória.")
    @PastOrPresent(message = "A data de criação não pode ser futura.")
    @Schema(description = "Data de criação do clube.", example = "1903-09-15", format = "yyyy-MM-dd", required = true)
    private LocalDate dataCriacao;

    @NotNull(message = "O status do clube é obrigatório.")
    @Schema(description = "Indica se o clube está ativo ou inativo.", example = "true", required = true)
    private Boolean ativo;

}
