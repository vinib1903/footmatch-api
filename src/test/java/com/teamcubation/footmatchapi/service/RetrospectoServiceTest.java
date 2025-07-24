package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.dto.response.PartidaResponseDTO;
import com.teamcubation.footmatchapi.mapper.PartidaMapper;
import com.teamcubation.footmatchapi.repository.ClubeRepository;
import com.teamcubation.footmatchapi.repository.PartidaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class RetrospectoServiceTest {

    @Mock
    private PartidaRepository partidaRepository;
    @Mock
    private ClubeRepository clubeRepository;
    @Mock
    private PartidaMapper partidaMapper;

    @InjectMocks
    private RetrospectoService retrospectoService;

    @Test
    void testGetRetrospectClub() {

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

        Partida partida1 = Partida.builder()
                .id(1L)
                .mandante(gremio)
                .visitante(palmeiras)
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(0)
                .golsVisitante(0)
                .build();

        Partida partida2 = Partida.builder()
                .id(2L)
                .mandante(palmeiras)
                .visitante(gremio)
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(0)
                .golsVisitante(0)
                .build();

        Partida partida3 = Partida.builder()
                .id(3L)
                .mandante(fluminense)
                .visitante(gremio)
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(0)
                .golsVisitante(0)
                .build();

        List<Partida> partidas = List.of(partida1, partida2, partida3);

        when(clubeRepository.findById(1L))
                .thenReturn(Optional.of(gremio));

        when(partidaRepository.findAllByClube(gremio))
                .thenReturn(partidas);

        var result = retrospectoService.obterRetrospecto(1L, null);

        assertEquals(3, result.getPartidas());
        assertEquals(0, result.getVitorias());
        assertEquals(3, result.getEmpates());
        assertEquals(0, result.getDerrotas());
        assertEquals(0, result.getGolsMarcados());
        assertEquals(0, result.getGolsSofridos());

        verify(clubeRepository, times(1)).findById(1L);
        verify(partidaRepository, times(1)).findAllByClube(gremio);
    }

    @Test
    void testGetRetrospectClubWithFilter() {

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

        Partida partida1 = Partida.builder()
                .id(1L)
                .mandante(gremio)
                .visitante(palmeiras)
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(2)
                .golsVisitante(1)
                .build();

        Partida partida2 = Partida.builder()
                .id(2L)
                .mandante(palmeiras)
                .visitante(gremio)
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(1)
                .golsVisitante(1)
                .build();

        Partida partida3 = Partida.builder()
                .id(3L)
                .mandante(fluminense)
                .visitante(gremio)
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(2)
                .golsVisitante(1)
                .build();

        List<Partida> partidas = List.of(partida1, partida2, partida3);

        when(clubeRepository.findById(1L))
                .thenReturn(Optional.of(gremio));

        when(partidaRepository.findAllByClube(gremio))
                .thenReturn(partidas);

        var result = retrospectoService.obterRetrospecto(1L, "mandante");

        assertEquals(1, result.getPartidas());
        assertEquals(1, result.getVitorias());
        assertEquals(0, result.getEmpates());
        assertEquals(0, result.getDerrotas());
        assertEquals(2, result.getGolsMarcados());
        assertEquals(1, result.getGolsSofridos());

        verify(clubeRepository, times(1)).findById(1L);
        verify(partidaRepository, times(1)).findAllByClube(gremio);
    }

    @Test
    void testGetRetrospectVersusAdversary() {

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

        Clube botafogo = Clube.builder()
                .id(4L)
                .nome("Botafogo")
                .siglaEstado(SiglaEstado.RJ)
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Partida partida1 = Partida.builder()
                .id(1L)
                .mandante(gremio)
                .visitante(palmeiras)
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(2)
                .golsVisitante(1)
                .build();

        Partida partida2 = Partida.builder()
                .id(2L)
                .mandante(palmeiras)
                .visitante(gremio)
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(1)
                .golsVisitante(1)
                .build();

        Partida partida3 = Partida.builder()
                .id(3L)
                .mandante(fluminense)
                .visitante(gremio)
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(2)
                .golsVisitante(1)
                .build();

        Partida partida4 = Partida.builder()
                .id(4L)
                .mandante(gremio)
                .visitante(botafogo)
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(2)
                .golsVisitante(1)
                .build();

        Partida partida5 = Partida.builder()
                .id(5L)
                .mandante(botafogo)
                .visitante(gremio)
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(2)
                .golsVisitante(2)
                .build();

        List<Partida> partidas = List.of(partida1, partida2, partida3, partida4, partida5);

        Pageable pageable = PageRequest.of(0, 10);

        when(clubeRepository.findById(1L))
                .thenReturn(Optional.of(gremio));

        when(partidaRepository.findAllByClube(gremio))
                .thenReturn(partidas);

        var result = retrospectoService.obterRestrospectoAdversarios(1L, null, pageable);

        var palmeirasResults = result.getContent().stream()
                .filter(dto -> dto.getAdversarioNome().contains("Palmeiras"))
                .findFirst().orElseThrow();

        assertEquals(2, palmeirasResults.getPartidas());
        assertEquals(1, palmeirasResults.getVitorias());
        assertEquals(1, palmeirasResults.getEmpates());
        assertEquals(0, palmeirasResults.getDerrotas());
        assertEquals(3, palmeirasResults.getGolsMarcados());
        assertEquals(2, palmeirasResults.getGolsSofridos());

        var fluminenseResults = result.getContent().stream()
                .filter(dto -> dto.getAdversarioNome().contains("Fluminense"))
                .findFirst().orElseThrow();

        assertEquals(1, fluminenseResults.getPartidas());
        assertEquals(0, fluminenseResults.getVitorias());
        assertEquals(0, fluminenseResults.getEmpates());
        assertEquals(1, fluminenseResults.getDerrotas());
        assertEquals(1, fluminenseResults.getGolsMarcados());
        assertEquals(2, fluminenseResults.getGolsSofridos());

        verify(clubeRepository, times(1)).findById(1L);
        verify(partidaRepository, times(1)).findAllByClube(gremio);
    }

    @Test
    void testGetHeadToHead() {

        Clube gremio = Clube.builder()
                .id(1L)
                .nome("Grêmio")
                .siglaEstado(SiglaEstado.RS)
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Clube botafogo = Clube.builder()
                .id(2L)
                .nome("Botafogo")
                .siglaEstado(SiglaEstado.RS)
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Partida partida1 = Partida.builder()
                .id(1L)
                .mandante(gremio)
                .visitante(botafogo)
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(1)
                .build();

        Partida partida2 = Partida.builder()
                .id(2L)
                .mandante(botafogo)
                .visitante(gremio)
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(2)
                .golsVisitante(1)
                .build();

        Partida partida3 = Partida.builder()
                .id(3L)
                .mandante(botafogo)
                .visitante(gremio)
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(2)
                .golsVisitante(2)
                .build();

        Partida partida4 = Partida.builder()
                .id(4L)
                .mandante(gremio)
                .visitante(botafogo)
                .estadio(Estadio.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(4)
                .golsVisitante(3)
                .build();

        PartidaResponseDTO partida1Dto = PartidaResponseDTO.builder()
                .id(1L)
                .mandante(ClubeResponseDTO.builder().id(1L).build())
                .visitante(ClubeResponseDTO.builder().id(2L).build())
                .estadio(EstadioResponseDTO.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(1)
                .build();

        PartidaResponseDTO partida2Dto = PartidaResponseDTO.builder()
                .id(2L)
                .mandante(ClubeResponseDTO.builder().id(2L).build())
                .visitante(ClubeResponseDTO.builder().id(1L).build())
                .estadio(EstadioResponseDTO.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(2)
                .golsVisitante(1)
                .build();

        PartidaResponseDTO partida3Dto = PartidaResponseDTO.builder()
                .id(3L)
                .mandante(ClubeResponseDTO.builder().id(2L).build())
                .visitante(ClubeResponseDTO.builder().id(1L).build())
                .estadio(EstadioResponseDTO.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(2)
                .golsVisitante(2)
                .build();

        PartidaResponseDTO partida4Dto = PartidaResponseDTO.builder()
                .id(4L)
                .mandante(ClubeResponseDTO.builder().id(1L).build())
                .visitante(ClubeResponseDTO.builder().id(2L).build())
                .estadio(EstadioResponseDTO.builder().id(1L).build())
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(4)
                .golsVisitante(3)
                .build();

        List<Partida> partidas = List.of(partida1, partida2, partida3, partida4);

        when(clubeRepository.findById(1L))
                .thenReturn(Optional.of(gremio));

        when(clubeRepository.findById(2L))
                .thenReturn(Optional.of(botafogo));

        when(partidaRepository.findAllByClubes(gremio, botafogo))
                .thenReturn(partidas);

        when(partidaMapper.toDto(partida1))
                .thenReturn(partida1Dto);

        when(partidaMapper.toDto(partida2))
                .thenReturn(partida2Dto);

        when(partidaMapper.toDto(partida3))
                .thenReturn(partida3Dto);

        when(partidaMapper.toDto(partida4))
                .thenReturn(partida4Dto);

        var result = retrospectoService.obterConfrontoDireto(1L, 2L, null);

        assertEquals(4, result.clube.getPartidas());
        assertEquals(4, result.adversario.getPartidas());
        assertEquals(2, result.clube.getVitorias());
        assertEquals(1, result.adversario.getVitorias());
        assertEquals(1, result.clube.getDerrotas());
        assertEquals(1, result.clube.getEmpates());
        assertEquals(1, result.adversario.getEmpates());
        assertEquals(2, result.adversario.getDerrotas());
        assertEquals(10, result.clube.getGolsMarcados());
        assertEquals(8, result.adversario.getGolsMarcados());
        assertEquals(8, result.clube.getGolsSofridos());
        assertEquals(10, result.adversario.getGolsSofridos());

        verify(clubeRepository, times(1)).findById(1L);
        verify(clubeRepository, times(1)).findById(2L);
        verify(partidaRepository, times(1)).findAllByClubes(gremio, botafogo);
        verify(partidaMapper, times(1)).toDto(partida1);
        verify(partidaMapper, times(1)).toDto(partida2);
        verify(partidaMapper, times(1)).toDto(partida3);
        verify(partidaMapper, times(1)).toDto(partida4);
    }

    @Test
    void testGetRankingOrderByPontos() {

        Clube gremio = Clube.builder()
                .id(1L)
                .nome("Grêmio")
                .siglaEstado(SiglaEstado.RS)
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Clube fluminense = Clube.builder()
                .id(2L)
                .nome("Fluminense")
                .siglaEstado(SiglaEstado.RJ)
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Clube botafogo = Clube.builder()
                .id(2L)
                .nome("Botafogo")
                .siglaEstado(SiglaEstado.RS)
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Clube palmeiras = Clube.builder()
                .id(3L)
                .nome("Palmeiras")
                .siglaEstado(SiglaEstado.SP)
                .dataCriacao(LocalDate.of(1914, 8, 26))
                .ativo(true)
                .build();

        Estadio arena = Estadio.builder()
                .id(1L)
                .nome("Arena do Grêmio")
                .build();

        Partida partida1 = Partida.builder()
                .id(1L)
                .mandante(gremio)
                .visitante(botafogo)
                .estadio(arena)
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(3)
                .golsVisitante(1)
                .build();

        Partida partida2 = Partida.builder()
                .id(2L)
                .mandante(botafogo)
                .visitante(gremio)
                .estadio(arena)
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(2)
                .golsVisitante(1)
                .build();

        Partida partida3 = Partida.builder()
                .id(3L)
                .mandante(palmeiras)
                .visitante(gremio)
                .estadio(arena)
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(2)
                .golsVisitante(2)
                .build();

        Partida partida4 = Partida.builder()
                .id(4L)
                .mandante(palmeiras)
                .visitante(botafogo)
                .estadio(arena)
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(4)
                .golsVisitante(3)
                .build();

        Partida partida5 = Partida.builder()
                .id(5L)
                .mandante(gremio)
                .visitante(fluminense)
                .estadio(arena)
                .dataHora(LocalDateTime.of(2022, 12, 18, 20, 0))
                .golsMandante(4)
                .golsVisitante(3)
                .build();

        when(clubeRepository.findAll())
                .thenReturn(List.of(gremio, botafogo, palmeiras, fluminense));

        when(partidaRepository.findAllByClube(gremio))
                .thenReturn(List.of(partida1, partida2, partida3, partida5));

        when(partidaRepository.findAllByClube(botafogo))
                .thenReturn(List.of(partida1, partida2, partida4));

        when(partidaRepository.findAllByClube(palmeiras))
                .thenReturn(List.of(partida3, partida4));

        when(partidaRepository.findAllByClube(fluminense))
                .thenReturn(List.of(partida5));

        Pageable pageable = PageRequest.of(0, 10);

        var result = retrospectoService.obterRanking("", pageable);
        assertTrue(result.getContent().get(0).getPontos() >= result.getContent().get(1).getPontos());
        assertTrue(result.getContent().get(1).getPontos() >= result.getContent().get(2).getPontos());
        assertEquals(7, result.getContent().get(0).getPontos());

        verify(clubeRepository, times(1)).findAll();
        verify(partidaRepository, times(1)).findAllByClube(gremio);
        verify(partidaRepository, times(1)).findAllByClube(botafogo);
        verify(partidaRepository, times(1)).findAllByClube(palmeiras);
        verify(partidaRepository, times(1)).findAllByClube(fluminense);
    }
}


