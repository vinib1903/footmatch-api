package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.repository.ClubeRepository;
import com.teamcubation.footmatchapi.repository.PartidaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class RestrospectoServiceTest {

    @Mock
    private PartidaRepository partidaRepository;
    @Mock
    private ClubeRepository clubeRepository;


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

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(gremio));
        when(partidaRepository.findAllByClube(gremio)).thenReturn(partidas);

        var result = retrospectoService.obterRetrospecto(1L, null);

        assertEquals(3, result.getPartidas());
        assertEquals(0, result.getVitorias());
        assertEquals(3, result.getEmpates());
        assertEquals(0, result.getDerrotas());
        assertEquals(0, result.getGolsMarcados());
        assertEquals(0, result.getGolsSofridos());
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

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(gremio));
        when(partidaRepository.findAllByClube(gremio)).thenReturn(partidas);

        var result = retrospectoService.obterRetrospecto(1L, "mandante");

        assertEquals(1, result.getPartidas());
        assertEquals(1, result.getVitorias());
        assertEquals(0, result.getEmpates());
        assertEquals(0, result.getDerrotas());
        assertEquals(2, result.getGolsMarcados());
        assertEquals(1, result.getGolsSofridos());
    }
}


