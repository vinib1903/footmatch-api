package com.teamcubation.footmatchapi.adapters.inbound.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamcubation.footmatchapi.application.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.application.usecase.EstadioUseCases;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EstadioControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private EstadioUseCases estadioUseCases;

    @InjectMocks
    private EstadioController estadioController;

    @BeforeEach
    void setUp() {
        // Configura o ObjectMapper para lidar com LocalDate
        objectMapper.findAndRegisterModules();
        
        mockMvc = MockMvcBuilders.standaloneSetup(estadioController).build();
    }

    @Test
    void createEstadio_ComDadosValidos_DeveRetornarAceito() throws Exception {
        // Arrange
        EstadioRequestDTO requestDTO = new EstadioRequestDTO();
        requestDTO.setNome("Arena do Grêmio");
        requestDTO.setCep("93300000");

        // Act & Assert
        mockMvc.perform(post("/api/v2/estadios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Estadio enviado para processamento assíncrono (Kafka)."));

        verify(estadioUseCases).solicitarCriacaoEstadio(any(EstadioRequestDTO.class));
    }

    @Test
    void createEstadio_ComDadosInvalidos_DeveRetornarBadRequest() throws Exception {
        // Arrange - Nome vazio e CEP inválido
        EstadioRequestDTO requestDTO = new EstadioRequestDTO();
        requestDTO.setNome(""); // Nome vazio é inválido
        requestDTO.setCep("123"); // CEP inválido

        // Act & Assert
        mockMvc.perform(post("/api/v2/estadios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateEstadio_ComDadosValidos_DeveRetornarAceito() throws Exception {
        // Arrange
        Long estadioId = 1L;
        EstadioRequestDTO requestDTO = new EstadioRequestDTO();
        requestDTO.setNome("Arena do Grêmio");
        requestDTO.setCep("93300000");

        // Act & Assert
        mockMvc.perform(put("/api/v2/estadios/" + estadioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Estadio enviado para processamento assíncrono (Kafka)."));

        verify(estadioUseCases).solicitarAtualizacaoEstadio(eq(estadioId), any(EstadioRequestDTO.class));
    }

    @Test
    void updateEstadio_ComDadosInvalidos_DeveRetornarBadRequest() throws Exception {
        // Arrange
        Long estadioId = 1L;
        EstadioRequestDTO requestDTO = new EstadioRequestDTO();
        requestDTO.setNome(""); // Nome vazio é inválido
        requestDTO.setCep("123"); // CEP inválido

        // Act & Assert
        mockMvc.perform(put("/api/v2/estadios/" + estadioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }
}
