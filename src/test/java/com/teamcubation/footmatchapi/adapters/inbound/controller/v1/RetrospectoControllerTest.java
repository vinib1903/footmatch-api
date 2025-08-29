package com.teamcubation.footmatchapi.adapters.inbound.controller.v1;

import com.teamcubation.footmatchapi.application.dto.response.ClubeRestrospectoAdversarioResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.ClubeRetrospectoResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.ConfrontoDiretoResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.RankingResponseDTO;
import com.teamcubation.footmatchapi.application.service.retrospecto.RetrospectoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RetrospectoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RetrospectoServiceImpl retrospectoService;

    @InjectMocks
    private RetrospectoController retrospectoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(retrospectoController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void getClubeRetrospect_ComDadosValidos_DeveRetornarOk() throws Exception {
        // Arrange
        Long clubeId = 1L;
        String papel = "ambos";
        
        ClubeRetrospectoResponseDTO responseDTO = new ClubeRetrospectoResponseDTO();
        responseDTO.setPartidas(10);
        responseDTO.setVitorias(5);
        responseDTO.setEmpates(3);
        responseDTO.setDerrotas(2);
        responseDTO.setGolsMarcados(15);
        responseDTO.setGolsSofridos(10);

        when(retrospectoService.obterRetrospecto(eq(clubeId), eq(papel))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/retrospectos/{id}", clubeId)
                        .param("papel", papel))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partidas").value(10))
                .andExpect(jsonPath("$.vitorias").value(5))
                .andExpect(jsonPath("$.empates").value(3))
                .andExpect(jsonPath("$.derrotas").value(2))
                .andExpect(jsonPath("$.golsMarcados").value(15))
                .andExpect(jsonPath("$.golsSofridos").value(10));
    }

    @Test
    void getClubeRetrospectVersusAdversarys_ComDadosValidos_DeveRetornarOk() throws Exception {
        // Arrange
        Long clubeId = 1L;
        String papel = "ambos";
        
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome"));
        Page<ClubeRestrospectoAdversarioResponseDTO> page = new PageImpl<>(
                Collections.singletonList(new ClubeRestrospectoAdversarioResponseDTO()),
                pageable,
                1
        );

        when(retrospectoService.obterRestrospectoAdversarios(eq(clubeId), eq(papel), any(Pageable.class))).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/v1/retrospectos/{id}/contra-adversarios", clubeId)
                        .param("papel", papel)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "nome,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getConfrontosDiretos_ComDadosValidos_DeveRetornarOk() throws Exception {
        // Arrange
        Long clubeId = 1L;
        Long adversarioId = 2L;
        String papel = "ambos";
        
        ConfrontoDiretoResponseDTO responseDTO = new ConfrontoDiretoResponseDTO();
        // Configurar o DTO conforme necessário

        when(retrospectoService.obterConfrontoDireto(eq(clubeId), eq(adversarioId), eq(papel))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/retrospectos/{clubeId}/confrontos-diretos/{adversarioId}", clubeId, adversarioId)
                        .param("papel", papel))
                .andExpect(status().isOk());
    }

    @Test
    void getRanking_ComDadosValidos_DeveRetornarOk() throws Exception {
        // Arrange
        String criterio = "pontos";
        
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pontos").descending());
        Page<RankingResponseDTO> page = new PageImpl<>(
                Collections.singletonList(new RankingResponseDTO()),
                pageable,
                1
        );

        when(retrospectoService.obterRanking(eq(criterio), any(Pageable.class))).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/v1/retrospectos/ranking")
                        .param("criterio", criterio)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "pontos,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}
