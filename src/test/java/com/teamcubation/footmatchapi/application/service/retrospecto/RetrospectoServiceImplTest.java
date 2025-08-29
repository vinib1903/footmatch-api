package com.teamcubation.footmatchapi.application.service.retrospecto;

import com.teamcubation.footmatchapi.application.ports.out.ClubeRepository;
import com.teamcubation.footmatchapi.application.ports.out.PartidaRepository;
import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.utils.mapper.PartidaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetrospectoServiceImplTest {

    @Mock
    private ClubeRepository clubeRepository;

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private PartidaMapper partidaMapper;

    @InjectMocks
    private RetrospectoServiceImpl retrospectoService;

    private Clube clube1;
    private Clube clube2;
    private Estadio estadio;
    private Partida partida1;
    private Partida partida2;
    private Partida partida3;

    @BeforeEach
    void setUp() {
        // Configuração dos clubes
        clube1 = new Clube();
        clube1.setId(1L);
        clube1.setNome("Grêmio");
        clube1.setSiglaEstado(SiglaEstado.RS);
        clube1.setAtivo(true);

        clube2 = new Clube();
        clube2.setId(2L);
        clube2.setNome("Internacional");
        clube2.setSiglaEstado(SiglaEstado.RS);
        clube2.setAtivo(true);

        // Configuração do estádio
        estadio = new Estadio();
        estadio.setId(1L);
        estadio.setNome("Arena do Grêmio");

        // Configuração das partidas
        partida1 = new Partida();
        partida1.setId(1L);
        partida1.setMandante(clube1);
        partida1.setVisitante(clube2);
        partida1.setEstadio(estadio);
        partida1.setDataHora(LocalDateTime.now().minusDays(10));
        partida1.setGolsMandante(2);
        partida1.setGolsVisitante(1);

        partida2 = new Partida();
        partida2.setId(2L);
        partida2.setMandante(clube2);
        partida2.setVisitante(clube1);
        partida2.setEstadio(estadio);
        partida2.setDataHora(LocalDateTime.now().minusDays(5));
        partida2.setGolsMandante(1);
        partida2.setGolsVisitante(1);

        partida3 = new Partida();
        partida3.setId(3L);
        partida3.setMandante(clube1);
        partida3.setVisitante(clube2);
        partida3.setEstadio(estadio);
        partida3.setDataHora(LocalDateTime.now().minusDays(1));
        partida3.setGolsMandante(3);
        partida3.setGolsVisitante(0);
    }

    @Test
    void obterRetrospecto_ComDadosValidos_DeveRetornarRetrospecto() {
        // Arrange
        List<Partida> partidas = Arrays.asList(partida1, partida2, partida3);
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube1));
        when(partidaRepository.findAllByClube(clube1)).thenReturn(partidas);

        // Act
        var resultado = retrospectoService.obterRetrospecto(1L, "todos");

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.getPartidas());
        assertEquals(2, resultado.getVitorias());
        assertEquals(1, resultado.getEmpates());
        assertEquals(0, resultado.getDerrotas());
        assertEquals(6, resultado.getGolsMarcados());
        assertEquals(2, resultado.getGolsSofridos());
    }

    @Test
    void obterRetrospecto_ComIdInexistente_DeveLancarExcecao() {
        // Arrange
        when(clubeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, 
            () -> retrospectoService.obterRetrospecto(999L, "todos"));
    }

    @Test
    void obterRestrospectoAdversarios_ComDadosValidos_DeveRetornarPagina() {
        // Arrange
        List<Partida> partidas = Arrays.asList(partida1, partida2, partida3);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("adversarioNome"));
        
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube1));
        when(partidaRepository.findAllByClube(clube1)).thenReturn(partidas);

        // Act
        var resultado = retrospectoService.obterRestrospectoAdversarios(1L, "todos", pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Internacional-RS", resultado.getContent().get(0).getAdversarioNome());
        assertEquals(3, resultado.getContent().get(0).getPartidas());
        assertEquals(2, resultado.getContent().get(0).getVitorias());
        assertEquals(1, resultado.getContent().get(0).getEmpates());
        assertEquals(0, resultado.getContent().get(0).getDerrotas());
        assertEquals(6, resultado.getContent().get(0).getGolsMarcados());
        assertEquals(2, resultado.getContent().get(0).getGolsSofridos());
    }

    @Test
    void obterConfrontoDireto_ComDadosValidos_DeveRetornarConfronto() {
        // Arrange
        List<Partida> partidas = Arrays.asList(partida1, partida2, partida3);
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube1));
        when(clubeRepository.findById(2L)).thenReturn(Optional.of(clube2));
        when(partidaRepository.findAllByClubes(clube1, clube2)).thenReturn(partidas);

        // Act
        var resultado = retrospectoService.obterConfrontoDireto(1L, 2L, "todos");

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getClube());
        assertNotNull(resultado.getAdversario());
        assertEquals(3, resultado.getPartidas().size());
        assertEquals(2, resultado.getClube().getVitorias());
        assertEquals(1, resultado.getClube().getEmpates());
        assertEquals(6, resultado.getClube().getGolsMarcados());
        assertEquals(2, resultado.getClube().getGolsSofridos());
    }

    @Test
    void obterRanking_ComDadosValidos_DeveRetornarRanking() {
        // Arrange
        List<Clube> clubes = Arrays.asList(clube1, clube2);
        List<Partida> partidasClube1 = Arrays.asList(partida1, partida3); // 2 vitórias
        List<Partida> partidasClube2 = Arrays.asList(partida2); // 1 empate
        
        when(clubeRepository.findAll()).thenReturn(clubes);
        when(partidaRepository.findAllByClube(clube1)).thenReturn(partidasClube1);
        when(partidaRepository.findAllByClube(clube2)).thenReturn(partidasClube2);

        // Act
        var resultado = retrospectoService.obterRanking("pontos", PageRequest.of(0, 10));

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        
        // Verifica se o clube1 está em primeiro (mais pontos)
        assertEquals("Grêmio-RS", resultado.getContent().get(0).getNome());
        assertEquals(6, resultado.getContent().get(0).getPontos());
        
        // Verifica se o clube2 está em segundo
        assertEquals("Internacional-RS", resultado.getContent().get(1).getNome());
        assertEquals(1, resultado.getContent().get(1).getPontos());
    }
}
