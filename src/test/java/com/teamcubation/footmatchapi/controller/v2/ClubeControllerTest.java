package com.teamcubation.footmatchapi.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamcubation.footmatchapi.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.service.kafka.ClubeServiceKafka;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClubeController.class)
class ClubeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClubeServiceKafka clubeServiceKafka;

    final String BASE_URL = "/api/v2/clubes";

    @Test
    void testCreateClubWhenDataIsValid_shouldReturnIsAccepted() throws Exception {

        ClubeRequestDTO requestDTO = ClubeRequestDTO.builder()
                .nome("Grêmio")
                .siglaEstado("RS")
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        doNothing().when(clubeServiceKafka).enviarClubeParaFilaCriacao(requestDTO);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isAccepted());

        verify(clubeServiceKafka).enviarClubeParaFilaCriacao(requestDTO);
    }

    @Test
    void testCreateClubWhenDataIsInvalid_shouldReturnBadRequest() throws Exception {

        ClubeRequestDTO requestDTO = ClubeRequestDTO.builder()
                .nome("")
                .siglaEstado("RS")
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(clubeServiceKafka, never()).enviarClubeParaFilaCriacao(requestDTO);
    }

    @Test
    void testUpdateClub_shouldReturnIsAccepted() throws Exception {

        ClubeRequestDTO requestDTO = ClubeRequestDTO.builder()
                .nome("Grêmio")
                .siglaEstado("RS")
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        doNothing().when(clubeServiceKafka).enviarClubeParaFilaAtualizacao(1L, requestDTO);

        mockMvc.perform(put(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isAccepted());

        verify(clubeServiceKafka).enviarClubeParaFilaAtualizacao(1L, requestDTO);
    }

    @Test
    void testUpdateClubWhenDataIsInvalid_shouldReturnBadRequest() throws Exception {

        ClubeRequestDTO requestDTO = ClubeRequestDTO.builder()
                .nome("Grêmio")
                .siglaEstado("A")
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        mockMvc.perform(put(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(clubeServiceKafka, never()).enviarClubeParaFilaAtualizacao(1L, requestDTO);
    }

    @Test
    void testDeleteClub_shouldReturnIsAccepted() throws Exception {

        doNothing().when(clubeServiceKafka).enviarClubeParaFilaExclusao(2L);

        mockMvc.perform(delete(BASE_URL + "/{id}", 2L))
                .andDo(print())
                .andExpect(status().isAccepted());

        verify(clubeServiceKafka).enviarClubeParaFilaExclusao(2L);
    }
}