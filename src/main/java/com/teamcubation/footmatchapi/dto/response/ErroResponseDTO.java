package com.teamcubation.footmatchapi.dto.response;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ErroResponseDTO {

    private int status;
    private List<String> errors;
}
