package com.teamcubation.footmatchapi.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamcubation.footmatchapi.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.service.kafka.EstadioServiceKafka;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstadioController.class)
class EstadioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EstadioServiceKafka estadioServiceKafka;

    final String BASE_URL = "/api/v2/estadios";

    @Test
    void testCreateStadiumWhenDataIsValid_shouldReturnIsAccepted() throws Exception {

        EstadioRequestDTO requestDTO = EstadioRequestDTO.builder()
                        .nome("Arena do Grêmio")
                        .cep("93300000")
                        .build();

        doNothing().when(estadioServiceKafka).enviarEstadioParaFilaCriacao(requestDTO);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isAccepted());

        verify(estadioServiceKafka).enviarEstadioParaFilaCriacao(requestDTO);
    }

    @Test
    void testCreateStadiumWhenDataIsInvalid_shouldReturnBadRequest() throws Exception {

        EstadioRequestDTO requestDTO = EstadioRequestDTO.builder()
                .nome("Arena do Grêmio")
                .cep("9921")
                .build();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(estadioServiceKafka, never()).enviarEstadioParaFilaCriacao(requestDTO);
    }

    @Test
    void testUpdateClub_shouldReturnIsAccepted() throws Exception {

        EstadioRequestDTO requestDTO = EstadioRequestDTO.builder()
                .nome("Arena do Grêmio")
                .cep("93300000")
                .build();

        doNothing().when(estadioServiceKafka).enviarEstadioParaFilaAtualizacao(4L, requestDTO);

        mockMvc.perform(put(BASE_URL + "/{id}", 4L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isAccepted());

        verify(estadioServiceKafka).enviarEstadioParaFilaAtualizacao(4L, requestDTO);
    }

    @Test
    void testUpdateStadiumWhenDataIsInvalid_shouldReturnBadRequest() throws Exception {

        EstadioRequestDTO requestDTO = EstadioRequestDTO.builder()
                .nome("A")
                .cep("93300-000")
                .build();

        mockMvc.perform(put(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(estadioServiceKafka, never()).enviarEstadioParaFilaAtualizacao(1L, requestDTO);
    }
}