package com.teamcubation.footmatchapi.controller;

import com.teamcubation.footmatchapi.dto.response.*;
import com.teamcubation.footmatchapi.service.RetrospectoService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(RetrospectoController.class)
public class RetrospectoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetrospectoService retrospectoService;

    final String BASE_URL = "/api/v1/retrospectos";


    @Test
    void testGetClubRetrospectById() throws Exception {

        ClubeRetrospectoResponseDTO response = ClubeRetrospectoResponseDTO.builder()
                .partidas(6)
                .vitorias(2)
                .empates(3)
                .derrotas(1)
                .golsMarcados(13)
                .golsSofridos(7)
                .build();

        when(retrospectoService.obterRetrospecto(eq(1L), anyString()))
                .thenReturn(response);

        mockMvc.perform(get(BASE_URL + "/1")
                        .param("papel", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partidas").value(6))
                .andExpect(jsonPath("$.vitorias").value(2))
                .andExpect(jsonPath("$.empates").value(3))
                .andExpect(jsonPath("$.derrotas").value(1))
                .andExpect(jsonPath("$.golsMarcados").value(13))
                .andExpect(jsonPath("$.golsSofridos").value(7));

        verify(retrospectoService).obterRetrospecto(eq(1L), anyString());
    }

    @Test
    void testGetClubRetrospectVersusAdversaries() throws Exception {

        ClubeRestrospectoAdversarioResponseDTO gremio = ClubeRestrospectoAdversarioResponseDTO.builder()
                .adversarioNome("Grêmio")
                .partidas(6)
                .vitorias(2)
                .empates(0)
                .derrotas(4)
                .golsMarcados(2)
                .golsSofridos(8)
                .build();

        ClubeRestrospectoAdversarioResponseDTO palmeiras = ClubeRestrospectoAdversarioResponseDTO.builder()
                .adversarioNome("Palmeiras")
                .partidas(6)
                .vitorias(4)
                .empates(1)
                .derrotas(1)
                .golsMarcados(9)
                .golsSofridos(4)
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome"));

        when(retrospectoService.obterRestrospectoAdversarios(eq(1L), anyString(), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(gremio, palmeiras), pageable, 2));

        mockMvc.perform(get(BASE_URL + "/1/contra-adversarios")
                .param("papel", "")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].adversarioNome").value("Grêmio"))
                .andExpect(jsonPath("$.content[0].partidas").value(6))
                .andExpect(jsonPath("$.content[0].vitorias").value(2))
                .andExpect(jsonPath("$.content[0].empates").value(0))
                .andExpect(jsonPath("$.content[0].derrotas").value(4))
                .andExpect(jsonPath("$.content[0].golsMarcados").value(2))
                .andExpect(jsonPath("$.content[0].golsSofridos").value(8))
                .andExpect(jsonPath("$.content[1].adversarioNome").value("Palmeiras"))
                .andExpect(jsonPath("$.content[1].partidas").value(6))
                .andExpect(jsonPath("$.content[1].vitorias").value(4))
                .andExpect(jsonPath("$.content[1].empates").value(1))
                .andExpect(jsonPath("$.content[1].derrotas").value(1))
                .andExpect(jsonPath("$.content[1].golsMarcados").value(9))
                .andExpect(jsonPath("$.content[1].golsSofridos").value(4));

        verify(retrospectoService).obterRestrospectoAdversarios(eq(1L), anyString(), eq(pageable));
    }

    @Test
    void testGetHeadToHead() throws Exception {

        ConfrontoDiretoResponseDTO response = ConfrontoDiretoResponseDTO.builder()
                .clube(ClubeRestrospectoAdversarioResponseDTO.builder().build())
                .adversario(ClubeRestrospectoAdversarioResponseDTO.builder().build())
                .partidas(List.of(PartidaResponseDTO.builder().build()))
                .build();

        when(retrospectoService.obterConfrontoDireto(eq(1L), eq(2L), anyString()))
                .thenReturn(response);

        mockMvc.perform(get(BASE_URL + "/1/confrontos-diretos/2")
                        .param("papel", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clube").exists())
                .andExpect(jsonPath("$.adversario").exists())
                .andExpect(jsonPath("$.partidas[0]").exists());

        verify(retrospectoService).obterConfrontoDireto(eq(1L), eq(2L), anyString());
    }

    @Test
    void testGetRanking() throws Exception {

        RankingResponseDTO gremio = RankingResponseDTO.builder()
                .nome("Grêmio")
                .pontos(25)
                .vitorias(8)
                .empates(1)
                .golsMarcados(20)
                .partidas(10)
                .build();

        RankingResponseDTO palmeiras = RankingResponseDTO.builder()
                .nome("Palmeiras")
                .pontos(22)
                .vitorias(7)
                .empates(1)
                .golsMarcados(18)
                .partidas(10)
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        when(retrospectoService.obterRanking(eq("pontos"), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(gremio, palmeiras), pageable, 2));

        mockMvc.perform(get(BASE_URL + "/ranking")
                        .param("criterio", "pontos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Grêmio"))
                .andExpect(jsonPath("$.content[0].pontos").value(25))
                .andExpect(jsonPath("$.content[1].nome").value("Palmeiras"))
                .andExpect(jsonPath("$.content[1].pontos").value(22));

        verify(retrospectoService).obterRanking(eq("pontos"), eq(pageable));
    }
}
