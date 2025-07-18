package com.teamcubation.footmatchapi.controller;

import com.teamcubation.footmatchapi.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.dto.response.PartidaResponseDTO;
import com.teamcubation.footmatchapi.service.PartidaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PartidaController.class)
public class PartidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartidaService partidaService;

    final String BASE_URL = "/api/v1/partidas";

    @Test
    void testCreateMatchWhenDataIsValid() throws Exception {

        PartidaResponseDTO response = PartidaResponseDTO.builder()
                .id(1L)
                .mandante(mock(ClubeResponseDTO.class))
                .visitante(mock(ClubeResponseDTO.class))
                .estadio(mock(EstadioResponseDTO.class))
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(5)
                .build();

        when(partidaService.criarPartida(any(PartidaRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mandanteId\": 1, \"visitanteId\": 2, \"estadioId\": 3, \"dataHora\": \"2022-12-18T20:00\", \"golsMandante\": 3, \"golsVisitante\": 5}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\": 1, \"mandante\": {}, \"visitante\": {}, \"estadio\": {}, \"dataHora\": \"2022-12-18T20:00:00\", \"golsMandante\": 3, \"golsVisitante\": 5}"));

        verify(partidaService, times(1)).criarPartida(any(PartidaRequestDTO.class));
    }

    @Test
    void testSearchMatches() throws Exception {

        PartidaResponseDTO partida1 = PartidaResponseDTO.builder()
                .id(1L)
                .mandante(mock(ClubeResponseDTO.class))
                .visitante(mock(ClubeResponseDTO.class))
                .estadio(mock(EstadioResponseDTO.class))
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(5)
                .build();

        PartidaResponseDTO partida2 = PartidaResponseDTO.builder()
                .id(2L)
                .mandante(mock(ClubeResponseDTO.class))
                .visitante(mock(ClubeResponseDTO.class))
                .estadio(mock(EstadioResponseDTO.class))
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(5)
                .build();

        PartidaResponseDTO partida3 = PartidaResponseDTO.builder()
                .id(1L)
                .mandante(mock(ClubeResponseDTO.class))
                .visitante(mock(ClubeResponseDTO.class))
                .estadio(mock(EstadioResponseDTO.class))
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(5)
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        when(partidaService.obterPartidas(any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(partida1, partida2, partida3), pageable, 3));

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[2].id").value(1L));

        verify(partidaService, times(1)).obterPartidas(any(), any(), any(), any(), any(Pageable.class));
    }

    @Test
    void testGetMatchById() throws Exception {

        PartidaResponseDTO partida = PartidaResponseDTO.builder()
                .id(1L)
                .mandante(mock(ClubeResponseDTO.class))
                .visitante(mock(ClubeResponseDTO.class))
                .estadio(mock(EstadioResponseDTO.class))
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(5)
                .build();

        when(partidaService.obterPartidaPorId(1L))
                .thenReturn(partida);

        mockMvc.perform(get(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.golsMandante").value(3));

        verify(partidaService, times(1)).obterPartidaPorId(1L);
    }

    @Test
    void testUpdateMatch() throws Exception {

        PartidaResponseDTO response = PartidaResponseDTO.builder()
                .id(1L)
                .mandante(mock(ClubeResponseDTO.class))
                .visitante(mock(ClubeResponseDTO.class))
                .estadio(mock(EstadioResponseDTO.class))
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(5)
                .build();

        when(partidaService.atualizarPartida(eq(1L), any(PartidaRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mandanteId\": 1, \"visitanteId\": 2, \"estadioId\": 3, \"dataHora\": \"2022-12-18T20:00\", \"golsMandante\": 3, \"golsVisitante\": 5}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.golsVisitante").value(5));

        verify(partidaService, times(1)).atualizarPartida(eq(1L), any(PartidaRequestDTO.class));
    }

    @Test
    void testDeleteMatch() throws Exception {

        mockMvc.perform(delete(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(partidaService, times(1)).deletarPartida(eq(1L));
    }

}
