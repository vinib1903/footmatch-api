package com.teamcubation.footmatchapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartidaRequestDTO {

    @NotNull (message = "O ID do clube mandante é obrigatório.")
    private Long mandanteId;

    @NotNull(message = "O ID do clube visitante é obrigatório.")
    private Long visitanteId;

    @NotNull(message = "O ID do estádio é obrigatório.")
    private Long estadoId;

    @NotNull(message = "A data e hora da partida são obrigatórias.")
    @PastOrPresent(message = "A data e hora da partida não podem ser futuras.")
    private LocalDateTime dataHora;

    @NotNull(message = "O número de gols do mandante é obrigatório.")
    @Min(value = 0, message = "O número de gols do mandante não pode ser negativo.")
    private Integer golsMandante;

    @NotNull(message = "O número de gols do visitante é obrigatório.")
    @Min(value = 0, message = "O número de gols do visitante não pode ser negativo.")
    private Integer golsVisitante;
}
