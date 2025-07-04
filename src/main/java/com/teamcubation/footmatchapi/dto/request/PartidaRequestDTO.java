package com.teamcubation.footmatchapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartidaRequestDTO {

    @NotBlank(message = "O ID do clube mandante é obrigatório.")
    private Long mandanteId;

    @NotBlank(message = "O ID do clube visitante é obrigatório.")
    private Long visitanteId;

    @NotBlank(message = "O ID do estádio é obrigatório.")
    private Long estadioId;

    @NotBlank(message = "A data e hora da partida são obrigatórias.")
    @PastOrPresent(message = "A data e hora da partida não podem ser futuras.")
    private LocalDateTime dataHora;

    @NotBlank(message = "O número de gols do mandante é obrigatório.")
    @Min(value = 0, message = "O número de gols do mandante não pode ser negativo.")
    private Integer golsMandante;

    @NotBlank(message = "O número de gols do visitante é obrigatório.")
    @Min(value = 0, message = "O número de gols do visitante não pode ser negativo.")
    private Integer golsVisitante;
}
