package com.teamcubation.footmatchapi.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamcubation.footmatchapi.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.service.kafka.PartidaServiceKafka;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartidaController.class)
class PartidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PartidaServiceKafka partidaServiceKafka;

    final String BASE_URL = "/api/v2/partidas";

    @Test
    void testCreateMatchWhenDataIsValid_shouldReturnIsAccepted() throws Exception {

        PartidaRequestDTO requestDTO = PartidaRequestDTO.builder()
                .mandanteId(1L)
                .visitanteId(3L)
                .estadioId(7L)
                .golsMandante(3)
                .golsVisitante(5)
                .dataHora(LocalDateTime.now())
                .build();

        doNothing().when(partidaServiceKafka).enviarPartidaParaFilaCriacao(requestDTO);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isAccepted());

        verify(partidaServiceKafka).enviarPartidaParaFilaCriacao(requestDTO);
    }

    @Test
    void testCreateMatchWhenDataIsInvalid_shouldReturnBadRequest() throws Exception {

        PartidaRequestDTO requestDTO = PartidaRequestDTO.builder()
                .mandanteId(1L)
                .visitanteId(3L)
                .estadioId(7L)
                .golsMandante(3)
                .golsVisitante(5)
                .dataHora(LocalDateTime.now().plusDays(1))
                .build();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(partidaServiceKafka, never()).enviarPartidaParaFilaCriacao(requestDTO);
    }

    @Test
    void testUpdateMatch_shouldReturnIsAccepted() throws Exception {

        PartidaRequestDTO requestDTO = PartidaRequestDTO.builder()
                .mandanteId(1L)
                .visitanteId(3L)
                .estadioId(7L)
                .golsMandante(3)
                .golsVisitante(5)
                .dataHora(LocalDateTime.now())
                .build();

        doNothing().when(partidaServiceKafka).enviarPartidaParaFilaAtualizacao(1L, requestDTO);

        mockMvc.perform(put(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isAccepted());

        verify(partidaServiceKafka).enviarPartidaParaFilaAtualizacao(1L, requestDTO);
    }

    @Test
    void testUpdateMatchWhenDataIsInvalid_shouldReturnBadRequest() throws Exception {

        PartidaRequestDTO requestDTO = PartidaRequestDTO.builder()
                .mandanteId(1L)
                .visitanteId(3L)
                .estadioId(7L)
                .golsMandante(-3)
                .golsVisitante(5)
                .dataHora(LocalDateTime.now())
                .build();

        mockMvc.perform(put(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(partidaServiceKafka, never()).enviarPartidaParaFilaAtualizacao(1L, requestDTO);
    }

    @Test
    void testDeleteMatch_shouldReturnIsAccepted() throws Exception {

        doNothing().when(partidaServiceKafka).enviarPartidaParaFilaExclusao(2L);

        mockMvc.perform(delete(BASE_URL + "/{id}", 2L))
                .andDo(print())
                .andExpect(status().isAccepted());

        verify(partidaServiceKafka).enviarPartidaParaFilaExclusao(2L);
    }
}