package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.application.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.PartidaResponseDTO;
import com.teamcubation.footmatchapi.utils.mapper.PartidaMapper;
import com.teamcubation.footmatchapi.adapters.outbound.repository.PartidaRepository;
import com.teamcubation.footmatchapi.application.service.clube.ClubeService;
import com.teamcubation.footmatchapi.application.service.estadio.EstadioService;
import com.teamcubation.footmatchapi.application.service.partida.PartidaService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.*;

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

        when(clubeService.validarExistenciaClube(1L))
                .thenReturn(mandante);

        when(clubeService.validarExistenciaClube(2L))
                .thenReturn(visitante);

        when(estadioService.validarExistenciaEstadio(1L))
                .thenReturn(estadio);

        when(partidaRepository.findAllByClube(mandante))
                .thenReturn(emptyList());

        when(partidaRepository.findAllByClube(visitante))
                .thenReturn(emptyList());

        when(partidaRepository.findAllByEstadioAndData(estadio, dto.getDataHora().toLocalDate()))
                .thenReturn(emptyList());

        when(partidaRepository.save(any(Partida.class)))
                .thenReturn(partida);

        when(partidaMapper.toDto(any(Partida.class)))
                .thenReturn(response);

        PartidaResponseDTO result = partidaService.criarPartida(dto);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals(mandanteDto, result.getMandante());
        assertEquals(visitanteDto, result.getVisitante());
        assertEquals(estadioDto, result.getEstadio());
        assertEquals(3, result.getGolsMandante());
        assertEquals(1, result.getGolsVisitante());
        assertEquals(response, result);

        verify(clubeService, times(1)).validarExistenciaClube(1L);
        verify(clubeService, times(1)).validarExistenciaClube(2L);
        verify(estadioService, times(1)).validarExistenciaEstadio(1L);
        verify(partidaRepository, times(1)).findAllByClube(mandante);
        verify(partidaRepository, times(1)).findAllByClube(visitante);
        verify(partidaRepository, times(1)).findAllByEstadioAndData(estadio, dto.getDataHora().toLocalDate());
        verify(partidaRepository, times(1)).save(any(Partida.class));
        verify(partidaMapper, times(1)).toDto(any(Partida.class));
    }

    @Test
    void testCreateMatchWhenClubIsInative() {

        Clube mandante = Clube.builder()
                .id(1L)
                .nome("Grêmio")
                .siglaEstado(SiglaEstado.RS)
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(false)
                .build();

        Clube visitante = Clube.builder()
                .id(2L)
                .nome("Palmeiras")
                .siglaEstado(SiglaEstado.SP)
                .dataCriacao(LocalDate.of(1914, 8, 26))
                .ativo(true)
                .build();

        Estadio estadio = Estadio.builder()
                .id(1L)
                .nome("Palestra Itália")
                .build();

        PartidaRequestDTO dto = PartidaRequestDTO.builder()
                .mandanteId(1L)
                .visitanteId(2L)
                .estadioId(1L)
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(1)
                .build();

        when(clubeService.validarExistenciaClube(1L))
                .thenReturn(mandante);

        when(clubeService.validarExistenciaClube(2L))
                .thenReturn(visitante);

        when(estadioService.validarExistenciaEstadio(1L))
                .thenReturn(estadio);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> partidaService.criarPartida(dto));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(org.springframework.http.HttpStatus.CONFLICT, ex.getStatusCode());
        assertEquals("Clube inativo.", ex.getReason());

        verify(clubeService, times(1)).validarExistenciaClube(1L);
        verify(clubeService, times(1)).validarExistenciaClube(2L);
        verify(estadioService, times(1)).validarExistenciaEstadio(1L);
    }

    @Test
    void testCreateMatchWhenStadiumIsOccupied() {

        Clube mandante = Clube.builder()
                .id(1L)
                .nome("Grêmio")
                .siglaEstado(SiglaEstado.RS)
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Clube visitante = Clube.builder()
                .id(2L)
                .nome("Palmeiras")
                .siglaEstado(SiglaEstado.SP)
                .dataCriacao(LocalDate.of(1914, 8, 26))
                .ativo(true)
                .build();

        Estadio estadio = Estadio.builder()
                .id(1L)
                .nome("Palestra Itália")
                .build();

        PartidaRequestDTO dto = PartidaRequestDTO.builder()
                .mandanteId(1L)
                .visitanteId(2L)
                .estadioId(1L)
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(1)
                .build();

        Partida partidaExistente = Partida.builder()
                .id(7L)
                .mandante(mandante)
                .visitante(visitante)
                .estadio(estadio)
                .dataHora(LocalDateTime.of(2022, 12, 18, 18, 0))
                .golsMandante(0)
                .golsVisitante(0)
                .build();

        when(clubeService.validarExistenciaClube(1L))
                .thenReturn(mandante);

        when(clubeService.validarExistenciaClube(2L))
                .thenReturn(visitante);

        when(estadioService.validarExistenciaEstadio(1L))
                .thenReturn(estadio);

        when(partidaRepository.findAllByClube(mandante))
                .thenReturn(emptyList());

        when(partidaRepository.findAllByClube(visitante))
                .thenReturn(emptyList());

        when(partidaRepository.findAllByEstadioAndData(estadio, LocalDate.of(2022, 12, 18)))
                .thenReturn(java.util.Collections.singletonList(partidaExistente));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> partidaService.criarPartida(dto));

        assertEquals(org.springframework.http.HttpStatus.CONFLICT, ex.getStatusCode());
        assertEquals("Estádio já possui partida neste dia.", ex.getReason());

        verify(clubeService, times(1)).validarExistenciaClube(1L);
        verify(clubeService, times(1)).validarExistenciaClube(2L);
        verify(estadioService, times(1)).validarExistenciaEstadio(1L);
        verify(partidaRepository, times(1)).findAllByClube(mandante);
        verify(partidaRepository, times(1)).findAllByClube(visitante);
        verify(partidaRepository, times(1)).findAllByEstadioAndData(estadio, LocalDate.of(2022, 12, 18));
    }

    @Test
    void testGetAllMatchesFilterByClubGremio() {
        Clube gremio = Clube.builder()
                .id(1L)
                .nome("Grêmio")
                .siglaEstado(SiglaEstado.RS)
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Clube palmeiras = Clube.builder()
                .id(2L)
                .nome("Palmeiras")
                .siglaEstado(SiglaEstado.SP)
                .dataCriacao(LocalDate.of(1914, 8, 26))
                .ativo(true)
                .build();

        Clube fluminense = Clube.builder()
                .id(3L)
                .nome("Fluminense")
                .siglaEstado(SiglaEstado.RJ)
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Estadio estadio = Estadio.builder()
                .id(1L)
                .nome("Palestra Itália")
                .build();

        Partida partida2 = Partida.builder()
                .id(2L)
                .mandante(fluminense)
                .visitante(gremio)
                .estadio(estadio)
                .dataHora(LocalDateTime.of(2022, 12, 15, 20, 0))
                .golsMandante(3)
                .golsVisitante(6)
                .build();

        Partida partida3 = Partida.builder()
                .id(3L)
                .mandante(gremio)
                .visitante(palmeiras)
                .estadio(estadio)
                .dataHora(LocalDateTime.of(2022, 12, 16, 20, 0))
                .golsMandante(3)
                .golsVisitante(2)
                .build();

        List<Partida> partidasGremio = List.of(partida2, partida3);

        Pageable pageable = PageRequest.of(0, 10);

        PartidaResponseDTO dto2 = PartidaResponseDTO.builder().id(2L).build();
        PartidaResponseDTO dto3 = PartidaResponseDTO.builder().id(3L).build();

        when(clubeService.validarExistenciaClube(1L)).thenReturn(gremio);

        when(partidaRepository.findPartidasWithFilters(eq(gremio), isNull(), isNull(), isNull(), eq(pageable)))
                .thenReturn(new PageImpl<>(partidasGremio));

        when(partidaMapper.toDto(partida2))
                .thenReturn(dto2);

        when(partidaMapper.toDto(partida3))
                .thenReturn(dto3);

        Page<PartidaResponseDTO> partidas = partidaService.obterPartidas(1L, null, null, null, pageable);

        log.info(partidas.getContent().toString());

        assertEquals(2, partidas.getContent().size());
        assertEquals(2L, partidas.getContent().get(0).getId());
        assertEquals(3L, partidas.getContent().get(1).getId());

        verify(partidaRepository, times(1)).findPartidasWithFilters(eq(gremio), isNull(), isNull(), isNull(), eq(pageable));
    }

    @Test
    void testGetMatchByValidId() {

        Partida partida = Partida.builder()
                .id(1L)
                .mandante(Clube.builder().id(1L).build())
                .visitante(Clube.builder().id(2L).build())
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(5)
                .build();

        PartidaResponseDTO dto = PartidaResponseDTO.builder()
                .id(1L)
                .mandante(ClubeResponseDTO.builder().id(1L).build())
                .visitante(ClubeResponseDTO.builder().id(2L).build())
                .estadio(EstadioResponseDTO.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(5)
                .build();

        when(partidaRepository.findById(1L))
                .thenReturn(Optional.of(partida));

        when(partidaMapper.toDto(partida))
                .thenReturn(dto);

        PartidaResponseDTO result = partidaService.obterPartidaPorId(1L);

        log.info(result.toString());

        assertEquals(1L, dto.getId());
        assertEquals(3, dto.getGolsMandante());
        assertEquals(5, dto.getGolsVisitante());

        verify(partidaRepository, times(1)).findById(1L);
        verify(partidaMapper, times(1)).toDto(partida);
    }

    @Test
    void testGetMatchByInvalidId() {

        when(partidaRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> partidaService.obterPartidaPorId(1L));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());

        verify(partidaRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateMatchWhenDataIsValid() {

        Partida partida = Partida.builder()
                .id(1L)
                .mandante(Clube.builder().id(1L).build())
                .visitante(Clube.builder().id(2L).build())
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(5)
                .build();

        PartidaRequestDTO requestDto = PartidaRequestDTO.builder()
                .mandanteId(1L)
                .visitanteId(2L)
                .estadioId(1L)
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(5)
                .build();

        PartidaResponseDTO responseDto = PartidaResponseDTO.builder()
                .id(1L)
                .mandante(ClubeResponseDTO.builder().id(1L).build())
                .visitante(ClubeResponseDTO.builder().id(2L).build())
                .estadio(EstadioResponseDTO.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(5)
                .build();

        when(partidaRepository.findById(1L))
                .thenReturn(Optional.of(partida));

        when(clubeService.validarExistenciaClube(1L))
                .thenReturn(Clube.builder()
                        .id(1L)
                        .dataCriacao(LocalDate.of(1903, 9, 15))
                        .ativo(true)
                        .build());

        when(clubeService.validarExistenciaClube(2L))
                .thenReturn(Clube.builder()
                        .id(2L)
                        .dataCriacao(LocalDate.of(1914, 8, 26))
                        .ativo(true)
                        .build());

        when(estadioService.validarExistenciaEstadio(1L))
                .thenReturn(Estadio.builder().id(1L).build());

        when(partidaRepository.findAllByClube(any(Clube.class)))
                .thenReturn(emptyList());

        when(partidaRepository.findAllByEstadioAndData(any(Estadio.class), any(LocalDate.class)))
                .thenReturn(emptyList());

        when(partidaRepository.save(any(Partida.class)))
                .thenReturn(partida);

        when(partidaMapper.toDto(any(Partida.class)))
                .thenReturn(responseDto);

        PartidaResponseDTO result = partidaService.atualizarPartida(1L, requestDto);

        log.info(result.toString());

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getMandante().getId());
        assertEquals(2L, result.getVisitante().getId());
        assertEquals(1L, result.getEstadio().getId());
        assertEquals(3, result.getGolsMandante());
        assertEquals(5, result.getGolsVisitante());
        assertEquals(responseDto, result);

        verify(partidaRepository, times(2)).findAllByClube(any(Clube.class));
    }

    @Test
    void testUpdateMatchWhenEqualsClubs() {

        Clube clube = Clube.builder()
                .id(1L)
                .nome("Grêmio")
                .siglaEstado(SiglaEstado.RS)
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Partida partida = Partida.builder()
                .id(1L)
                .mandante(clube)
                .visitante(clube)
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(0)
                .golsVisitante(0)
                .build();

        PartidaRequestDTO requestDto = PartidaRequestDTO.builder()
                .mandanteId(1L)
                .visitanteId(1L)
                .estadioId(1L)
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(0)
                .golsVisitante(0)
                .build();

        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));
        when(clubeService.validarExistenciaClube(1L)).thenReturn(clube);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> partidaService.atualizarPartida(1L, requestDto));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());

        verify(partidaRepository, times(0)).findAllByClube(any(Clube.class));
    }

    @Test
    void testDeleteMatchWhenDataIsValid() {

        Partida partida = Partida.builder()
                .id(1L)
                .build();

        when(partidaRepository.findById(1L))
                .thenReturn(Optional.of(partida));

        partidaService.deletarPartida(1L);

        verify(partidaRepository, times(1)).delete(partida);
    }

    @Test
    void testDeleteMatchWhenDataIsInvalid() {

        when(partidaRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> partidaService.deletarPartida(1L));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());

        verify(partidaRepository, times(1)).findById(1L);
    }
}
