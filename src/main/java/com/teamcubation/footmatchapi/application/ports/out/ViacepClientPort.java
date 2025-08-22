package com.teamcubation.footmatchapi.application.ports.out;

import com.teamcubation.footmatchapi.application.dto.response.ViaCepResponseDTO;

public interface ViacepClientPort {

    ViaCepResponseDTO buscarEnderecoPorCep(String cep);
}
