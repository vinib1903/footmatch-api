package com.teamcubation.footmatchapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ErroResponseDTO {

    @Schema(description = "Status HTTP da resposta.", example = "400")
    private int status;

    @Schema(description = "Mensagens de erro.", example = "[\"Erro 1\", \"Erro 2\"]")
    private List<String> errors;
}
