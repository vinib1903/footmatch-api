package com.teamcubation.footmatchapi.adapters.inbound.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamcubation.footmatchapi.application.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.application.usecase.ClubeUseCases;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClubeControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ClubeUseCases clubeUseCases;

    @InjectMocks
    private ClubeController clubeController;

    @BeforeEach
    void setUp() {
        // Configura o ObjectMapper para lidar com LocalDate
        objectMapper.findAndRegisterModules();
        
        mockMvc = MockMvcBuilders.standaloneSetup(clubeController).build();
    }

    @Test
    void createClube_ComDadosValidos_DeveRetornarAceito() throws Exception {
        // Arrange
        ClubeRequestDTO requestDTO = new ClubeRequestDTO();
        requestDTO.setNome("Grêmio");
        requestDTO.setSiglaEstado("RS");
        requestDTO.setAtivo(true);
        requestDTO.setDataCriacao(LocalDate.of(1903, 9, 15));

        // Act & Assert
        mockMvc.perform(post("/api/v2/clubes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Clube enviado para processamento assíncrono (Kafka)."));

        verify(clubeUseCases).solicitarCriacaoClube(any(ClubeRequestDTO.class));
    }

    @Test
    void createClube_ComDadosInvalidos_DeveRetornarBadRequest() throws Exception {
        // Arrange - Nome vazio é inválido
        ClubeRequestDTO requestDTO = new ClubeRequestDTO();
        requestDTO.setNome(""); // Nome vazio é inválido
        requestDTO.setSiglaEstado("RS");
        requestDTO.setAtivo(true);
        requestDTO.setDataCriacao(LocalDate.of(1903, 9, 15));

        // Act & Assert
        mockMvc.perform(post("/api/v2/clubes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateClube_ComDadosValidos_DeveRetornarAceito() throws Exception {
        // Arrange
        Long clubeId = 1L;
        ClubeRequestDTO requestDTO = new ClubeRequestDTO();
        requestDTO.setNome("Grêmio");
        requestDTO.setSiglaEstado("RS");
        requestDTO.setAtivo(true);
        requestDTO.setDataCriacao(LocalDate.of(1903, 9, 15));

        // Act & Assert
        mockMvc.perform(put("/api/v2/clubes/" + clubeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Ckube enviado para processamento assíncrono (Kafka)."));

        verify(clubeUseCases).solicitarAtualizacaoClube(eq(clubeId), any(ClubeRequestDTO.class));
    }

    @Test
    void inativarClube_ComIdValido_DeveRetornarAceito() throws Exception {
        // Arrange
        Long clubeId = 1L;
        String expectedResponse = "Clube enviado para processamento assíncrono (Kafka).";

        // Act & Assert
        mockMvc.perform(delete("/api/v2/clubes/" + clubeId))
                .andExpect(status().isAccepted())
                .andExpect(content().string(expectedResponse));

        verify(clubeUseCases).solicitarInativacaoClube(clubeId);
    }
}
