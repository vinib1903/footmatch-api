package com.teamcubation.footmatchapi.adapters.outbound.integration;

import com.teamcubation.footmatchapi.application.dto.response.ViaCepResponseDTO;

public interface ViacepClientPort {

    ViaCepResponseDTO buscarEnderecoPorCep(String cep);
}
