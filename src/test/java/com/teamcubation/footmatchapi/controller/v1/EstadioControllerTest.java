package com.teamcubation.footmatchapi.controller.v1;

import com.teamcubation.footmatchapi.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.dto.response.EnderecoResponseDTO;
import com.teamcubation.footmatchapi.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.service.estadio.EstadioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(EstadioController.class)
public class EstadioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EstadioService estadioService;

    final String BASE_URL = "/api/v1/estadios";

    @Test
    void testCreateStadiumWhenDataIsValid_shouldReturnIsGone() throws Exception {

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Olímpico\", \"cep\": \"93300000\"}"))
                .andDo(print())
                .andExpect(status().isGone());

        verify(estadioService, never()).criarEstadio(any(EstadioRequestDTO.class));
    }

    @Test
    void testCreateStadiumWhenDataIsInvalid_shouldReturnIsGone() throws Exception {

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"\", \"cep\": \"\"}"))
                .andDo(print())
                .andExpect(status().isGone());

        verify(estadioService, never()).criarEstadio(any(EstadioRequestDTO.class));
    }

    @Test
    void testSearchStadiums_shouldReturnPageOfStadiums() throws Exception {

        EstadioResponseDTO maracana = EstadioResponseDTO.builder()
                .id(1L)
                .nome("Maracanã")
                .endereco(EnderecoResponseDTO.builder()
                        .logradouro("Rua Fictícia")
                        .bairro("Bairro Fictício")
                        .localidade("Cidade Fictícia")
                        .uf("UF")
                        .cep("20271-150")
                        .build())
                .build();

        EstadioResponseDTO olimpico = EstadioResponseDTO.builder()
                .id(2L)
                .nome("Olímpico")
                .endereco(EnderecoResponseDTO.builder()
                        .logradouro("Rua Fictícia")
                        .bairro("Bairro Fictício")
                        .localidade("Cidade Fictícia")
                        .uf("UF")
                        .cep("90880-440")
                        .build())
                .build();

        EstadioResponseDTO palestra = EstadioResponseDTO.builder()
                .id(3L)
                .nome("Palestra Itália")
                .endereco(EnderecoResponseDTO.builder()
                        .logradouro("Rua Fictícia")
                        .bairro("Bairro Fictício")
                        .localidade("Cidade Fictícia")
                        .uf("UF")
                        .cep("05005-030")
                        .build())
                .build();

        List<EstadioResponseDTO> estadios = List.of(maracana, olimpico, palestra);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome").ascending());
        PageImpl<EstadioResponseDTO> page = new PageImpl<>(estadios, pageable, estadios.size());

        when(estadioService.obterEstadios(any(), eq(pageable))).thenReturn(page);

        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "nome,asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.content[0].nome").value("Maracanã"))
                .andExpect(jsonPath("$.content[1].nome").value("Olímpico"))
                .andExpect(jsonPath("$.content[2].nome").value("Palestra Itália"));

        verify(estadioService, times(1)).obterEstadios(any(), eq(pageable));
    }

    @Test
    void testGetStadiumById_shouldReturnIsOk() throws Exception {

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
    void testUpdateStadium_shouldReturnIsGone() throws Exception {

        mockMvc.perform(put(BASE_URL + "/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Olímpico Monumental\", \"cep\": \"93300000\"}"))
                .andDo(print())
                .andExpect(status().isGone());

        verify(estadioService, never()).atualizarEstadio(eq(7L), any(EstadioRequestDTO.class));
    }
}
