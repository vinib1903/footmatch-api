package com.teamcubation.footmatchapi.controller;

import com.teamcubation.footmatchapi.controller.v1.EstadioController;
import com.teamcubation.footmatchapi.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.service.EstadioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstadioController.class)
public class EstadioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstadioService estadioService;

    final String BASE_URL = "/api/v1/estadios";

    @Test
    void testCreateStadiumWhenDataIsValid() throws Exception {

        EstadioResponseDTO response = EstadioResponseDTO.builder()
                .id(1L)
                .nome("Olímpico")
                .build();

        when(estadioService.criarEstadio(any(EstadioRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Olímpico\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\": 1, \"nome\": \"Olímpico\"}"));

        verify(estadioService, times(1)).criarEstadio(any(EstadioRequestDTO.class));
    }

    @Test
    void testCreateStadiumWhenDataIsInvalid() throws Exception {

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(estadioService, never()).criarEstadio(any(EstadioRequestDTO.class));
    }

    @Test
    void testSearchStadiums() throws Exception {

        EstadioResponseDTO olimpico = EstadioResponseDTO.builder()
                .id(1L)
                .nome("Olímpico")
                .build();

        EstadioResponseDTO palestra = EstadioResponseDTO.builder()
                .id(2L)
                .nome("Palestra Itália")
                .build();

        EstadioResponseDTO maracana = EstadioResponseDTO.builder()
                .id(3L)
                .nome("Maracanã")
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome").ascending());

        when(estadioService.obterEstadios(any(String.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(olimpico, palestra, maracana), pageable, 3));

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].nome").value("Olímpico"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].nome").value("Palestra Itália"))
                .andExpect(jsonPath("$.content[2].id").value(3L))
                .andExpect(jsonPath("$.content[2].nome").value("Maracanã"));

        verify(estadioService, times(1)).obterEstadios(any(String.class), any(Pageable.class));
    }

    @Test
    void testGetStadiumById() throws Exception {

        EstadioResponseDTO olimpico = EstadioResponseDTO.builder()
                .id(7L)
                .nome("Olímpico")
                .build();

        when(estadioService.obterEstadioPorId(eq(7L)))
                .thenReturn(olimpico);

        mockMvc.perform(get(BASE_URL + "/7")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7L))
                .andExpect(jsonPath("$.nome").value("Olímpico"));

        verify(estadioService, times(1)).obterEstadioPorId(eq(7L));
    }

    @Test
    void testUpdateStadium() throws Exception {

        EstadioResponseDTO response = EstadioResponseDTO.builder()
                .id(7L)
                .nome("Olímpico")
                .build();

        when(estadioService.atualizarEstadio(eq(7L), any(EstadioRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put(BASE_URL + "/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Olímpico Monumental\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7L))
                .andExpect(jsonPath("$.nome").value("Olímpico"));

        verify(estadioService, times(1)).atualizarEstadio(eq(7L), any(EstadioRequestDTO.class));
    }
}
