package com.teamcubation.footmatchapi.adapters.outbound.integration;

import com.teamcubation.footmatchapi.application.dto.response.ViaCepResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ViacepClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public ViaCepResponseDTO buscarEnderecoPorCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        ViaCepResponseDTO response = restTemplate.getForObject(url, ViaCepResponseDTO.class);
        if (response == null || Boolean.TRUE.equals(response.getErro())) {
            throw new IllegalArgumentException("CEP inválido.");
        }
        return response;
    }
}
