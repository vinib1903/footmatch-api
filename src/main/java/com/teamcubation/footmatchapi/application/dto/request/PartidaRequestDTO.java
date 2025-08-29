package com.teamcubation.footmatchapi.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PartidaRequestDTO {

    @NotNull(message = "O ID do clube mandante é obrigatório.")
    @Schema(description = "Identificador único do clube mandante da partida.", example = "1", required = true)
    private Long mandanteId;

    @NotNull(message = "O ID do clube visitante é obrigatório.")
    @Schema(description = "Identificador único do clube visitante da partida.", example = "2", required = true)
    private Long visitanteId;

    @NotNull(message = "O ID do estádio é obrigatório.")
    @Schema(description = "Identificador único do estádio da partida.", example = "1", required = true)
    private Long estadioId;

    @NotNull(message = "A data e hora da partida são obrigatórias.")
    @PastOrPresent(message = "A data e hora da partida não podem ser futuras.")
    @Schema(description = "Data e hora da partida.", example = "2023-09-15T12:00:00", format = "yyyy-MM-dd'T'HH:mm:ss", required = true)
    private LocalDateTime dataHora;

    @NotNull(message = "O número de gols do mandante é obrigatório.")
    @Min(value = 0, message = "O número de gols do mandante não pode ser negativo.")
    @Schema(description = "Número de gols do clube mandante.", example = "3", required = true)
    private Integer golsMandante;

    @NotNull(message = "O número de gols do visitante é obrigatório.")
    @Schema(description = "Número de gols do clube visitante.", example = "0", required = true)
    @Min(value = 0, message = "O número de gols do visitante não pode ser negativo.")
    private Integer golsVisitante;
}
