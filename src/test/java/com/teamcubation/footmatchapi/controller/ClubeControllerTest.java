package com.teamcubation.footmatchapi.controller;

import com.teamcubation.footmatchapi.controller.v1.ClubeController;
import com.teamcubation.footmatchapi.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.service.ClubeService;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClubeController.class)
public class ClubeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClubeService clubeService;

    final String BASE_URL = "/api/v1/clubes";

    @Test
    void testCreateClubWhenDataIsValid() throws Exception {

        ClubeResponseDTO response = ClubeResponseDTO.builder()
                .id(1L)
                .nome("Grêmio")
                .siglaEstado("RS")
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        when(clubeService.criarClube(any(ClubeRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Grêmio\", \"siglaEstado\": \"RS\", \"dataCriacao\": \"1903-09-15\", \"ativo\": true}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\": 1, \"nome\": \"Grêmio\", \"siglaEstado\": \"RS\", \"dataCriacao\": \"1903-09-15\", \"ativo\": true}"));

        verify(clubeService, times(1)).criarClube(any(ClubeRequestDTO.class));
    }

    @Test
    void testCreateClubWhenDataIsInvalid() throws Exception {

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Grêmio\", \"siglaEstado\": \"\", \"dataCriacao\": \"1903-09-15\", \"ativo\": true}"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(clubeService, never()).criarClube(any(ClubeRequestDTO.class));
    }

    @Test
    void testSearchClubs() throws Exception {

        ClubeResponseDTO gremio = ClubeResponseDTO.builder()
                .id(1L)
                .nome("Grêmio")
                .siglaEstado("RS")
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        ClubeResponseDTO botafogo = ClubeResponseDTO.builder()
                .id(2L)
                .nome("Botafogo")
                .siglaEstado("RS")
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome").ascending());

        when(clubeService.obterClubes(any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(gremio, botafogo), pageable, 2));

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Grêmio"))
                .andExpect(jsonPath("$.content[1].nome").value("Botafogo"));

        verify(clubeService, times(1)).obterClubes(any(), any(), any(), any(Pageable.class));
    }

    @Test
    void testGetClubById() throws Exception {

        ClubeResponseDTO gremio = ClubeResponseDTO.builder()
                .id(1L)
                .nome("Grêmio")
                .siglaEstado("RS")
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        when(clubeService.obterClubePorId(1L))
                .thenReturn(gremio);

        mockMvc.perform(get(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Grêmio"));

        verify(clubeService, times(1)).obterClubePorId(1L);
    }

    @Test
    void testUpdateClub() throws Exception {

        ClubeResponseDTO response = ClubeResponseDTO.builder()
                .id(1L)
                .nome("Grêmio F.B.P.A.")
                .siglaEstado("RS")
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        when(clubeService.atualizarClube(eq(1L), any(ClubeRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Grêmio F.B.P.A.\", \"siglaEstado\": \"RS\", \"dataCriacao\": \"1903-09-15\", \"ativo\": true}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(content().json("{\"id\": 1, \"nome\": \"Grêmio F.B.P.A.\", \"siglaEstado\": \"RS\", \"dataCriacao\": \"1903-09-15\", \"ativo\": true}"));

        verify(clubeService, times(1)).atualizarClube(eq(1L), any(ClubeRequestDTO.class));
    }

    @Test
    void testDeleteClub() throws Exception {

        mockMvc.perform(delete(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(clubeService, times(1)).inativarClube(eq(1L));
    }
}
