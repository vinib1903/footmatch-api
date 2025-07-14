package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.dto.response.PartidaResponseDTO;
import com.teamcubation.footmatchapi.mapper.PartidaMapper;
import com.teamcubation.footmatchapi.repository.PartidaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class PartidaServiceTest {

    @Mock
    private PartidaRepository partidaRepository;
    @Mock
    private PartidaMapper partidaMapper;
    @Mock
    private EstadioService estadioService;
    @Mock
    private ClubeService clubeService;

    @InjectMocks
    private PartidaService partidaService;

    @Test
    void testCreateMatchWhenDataIsValid() {

        Clube mandante = Clube.builder()
                .id(1L)
                .nome("Grêmio")
                .siglaEstado(SiglaEstado.valueOf("RS"))
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        ClubeResponseDTO mandanteDto = ClubeResponseDTO.builder()
                .id(mandante.getId())
                .nome(mandante.getNome())
                .siglaEstado(mandante.getSiglaEstado().toString())
                .dataCriacao(mandante.getDataCriacao())
                .ativo(mandante.getAtivo())
                .build();

        Clube visitante = Clube.builder()
                .id(2L)
                .nome("Palmeiras")
                .siglaEstado(SiglaEstado.valueOf("SP"))
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        ClubeResponseDTO visitanteDto = ClubeResponseDTO.builder()
                .id(visitante.getId())
                .nome(visitante.getNome())
                .siglaEstado(visitante.getSiglaEstado().toString())
                .dataCriacao(visitante.getDataCriacao())
                .ativo(visitante.getAtivo())
                .build();

        Estadio estadio = Estadio.builder()
                .id(1L)
                .nome("Palestra Itália")
                .build();

        EstadioResponseDTO estadioDto = EstadioResponseDTO.builder()
                .id(estadio.getId())
                .nome(estadio.getNome())
                .build();

        PartidaRequestDTO dto = PartidaRequestDTO.builder()
                .mandanteId(1L)
                .visitanteId(2L)
                .estadioId(1L)
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(1)
                .build();

        Partida partida = Partida.builder()
                .id(5L)
                .mandante(mandante)
                .visitante(visitante)
                .estadio(estadio)
                .dataHora(dto.getDataHora())
                .golsMandante(dto.getGolsMandante())
                .golsVisitante(dto.getGolsVisitante())
                .build();

        PartidaResponseDTO response = PartidaResponseDTO.builder()
                .id(5L)
                .mandante(mandanteDto)
                .visitante(visitanteDto)
                .estadio(estadioDto)
                .dataHora(dto.getDataHora())
                .golsMandante(dto.getGolsMandante())
                .golsVisitante(dto.getGolsVisitante())
                .build();

        when(clubeService.validarExistenciaClube(1L)).thenReturn(mandante);
        when(clubeService.validarExistenciaClube(2L)).thenReturn(visitante);
        when(estadioService.validarExistenciaEstadio(1L)).thenReturn(estadio);
        when(partidaRepository.findAllByClube(mandante)).thenReturn(emptyList());
        when(partidaRepository.findAllByClube(visitante)).thenReturn(emptyList());
        when(partidaRepository.findAllByEstadioAndData(estadio, dto.getDataHora().toLocalDate())).thenReturn(emptyList());
        when(partidaRepository.save(any(Partida.class))).thenReturn(partida);
        when(partidaMapper.toDto(any(Partida.class))).thenReturn(response);

        PartidaResponseDTO result = partidaService.criarPartida(dto);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals(mandanteDto, result.getMandante());
        assertEquals(visitanteDto, result.getVisitante());
        assertEquals(estadioDto, result.getEstadio());
        assertEquals(3, result.getGolsMandante());
        assertEquals(1, result.getGolsVisitante());
        assertEquals(response, result);
    }


}
