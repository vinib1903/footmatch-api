package com.teamcubation.footmatchapi.adapters.inbound.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamcubation.footmatchapi.application.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.application.usecase.PartidaUseCases;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PartidaControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PartidaUseCases partidaUseCases;

    @InjectMocks
    private PartidaController partidaController;

    @BeforeEach
    void setUp() {
        // Configura o ObjectMapper para lidar com LocalDateTime
        objectMapper.findAndRegisterModules();
        
        mockMvc = MockMvcBuilders.standaloneSetup(partidaController).build();
    }

    @Test
    void createPartida_ComDadosValidos_DeveRetornarAceito() throws Exception {
        // Arrange
        PartidaRequestDTO requestDTO = PartidaRequestDTO.builder()
                .mandanteId(1L)
                .visitanteId(2L)
                .estadioId(1L)
                .dataHora(LocalDateTime.now().minusDays(1))
                .golsMandante(2)
                .golsVisitante(1)
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v2/partidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Partida enviada para processamento assíncrono (Kafka)."));

        verify(partidaUseCases).solicitarCriacaoPartida(any(PartidaRequestDTO.class));
    }

    @Test
    void createPartida_ComDadosInvalidos_DeveRetornarBadRequest() throws Exception {
        // Arrange - Dados inválidos: mandanteId nulo e data futura
        PartidaRequestDTO requestDTO = PartidaRequestDTO.builder()
                .mandanteId(null) // Inválido: nulo
                .visitanteId(2L)
                .estadioId(1L)
                .dataHora(LocalDateTime.now().plusDays(1)) // Inválido: data futura
                .golsMandante(-1) // Inválido: negativo
                .golsVisitante(1)
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v2/partidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePartida_ComDadosValidos_DeveRetornarAceito() throws Exception {
        // Arrange
        Long partidaId = 1L;
        PartidaRequestDTO requestDTO = PartidaRequestDTO.builder()
                .mandanteId(1L)
                .visitanteId(2L)
                .estadioId(1L)
                .dataHora(LocalDateTime.now().minusDays(1))
                .golsMandante(2)
                .golsVisitante(1)
                .build();

        // Act & Assert
        mockMvc.perform(put("/api/v2/partidas/" + partidaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Partida enviada para processamento assíncrono (Kafka)."));

        verify(partidaUseCases).solicitarAtualizacaoPartida(eq(partidaId), any(PartidaRequestDTO.class));
    }

    @Test
    void deletePartida_ComIdValido_DeveRetornarAceito() throws Exception {
        // Arrange
        Long partidaId = 1L;

        // Act & Assert
        mockMvc.perform(delete("/api/v2/partidas/" + partidaId))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Partida enviada para processamento assíncrono (Kafka)."));

        verify(partidaUseCases).solicitarExclusaoPartida(partidaId);
    }
}
