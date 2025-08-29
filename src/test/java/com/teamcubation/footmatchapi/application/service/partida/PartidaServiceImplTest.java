package com.teamcubation.footmatchapi.application.service.partida;

import com.teamcubation.footmatchapi.application.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.PartidaResponseDTO;
import com.teamcubation.footmatchapi.application.ports.out.PartidaEventsPort;
import com.teamcubation.footmatchapi.application.ports.out.PartidaRepository;
import com.teamcubation.footmatchapi.application.usecase.ClubeUseCases;
import com.teamcubation.footmatchapi.application.usecase.EstadioUseCases;
import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.domain.exceptions.EntidadeEmUsoException;
import com.teamcubation.footmatchapi.domain.exceptions.EntidadeNaoEncontradaException;
import com.teamcubation.footmatchapi.utils.mapper.PartidaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartidaServiceImplTest {

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private PartidaMapper partidaMapper;

    @Mock
    private EstadioUseCases estadioUseCases;

    @Mock
    private ClubeUseCases clubeUseCases;

    @Mock
    private PartidaEventsPort partidaEventsPort;

    @InjectMocks
    private PartidaServiceImpl partidaService;

    private Partida partida;
    private PartidaRequestDTO requestDTO;
    private PartidaResponseDTO responseDTO;
    private Clube mandante;
    private Clube visitante;
    private Estadio estadio;
    private ClubeResponseDTO mandanteResponseDTO;
    private ClubeResponseDTO visitanteResponseDTO;
    private EstadioResponseDTO estadioResponseDTO;
    
    private static final Long PARTIDA_ID = 1L;
    private static final Long CLUBE_MANDANTE_ID = 1L;
    private static final Long CLUBE_VISITANTE_ID = 2L;
    private static final Long ESTADIO_ID = 1L;
    private static final LocalDateTime DATA_HORA = LocalDateTime.of(2023, 10, 15, 16, 0);
    private static final int GOLS_MANDANTE = 2;
    private static final int GOLS_VISITANTE = 1;

    @BeforeEach
    void setUp() {
        // Configuração dos clubes
        mandante = new Clube();
        mandante.setId(CLUBE_MANDANTE_ID);
        mandante.setNome("Grêmio");
        mandante.setSiglaEstado(SiglaEstado.RS);
        mandante.setDataCriacao(LocalDate.of(1903, 9, 15));
        mandante.setAtivo(true);

        visitante = new Clube();
        visitante.setId(CLUBE_VISITANTE_ID);
        visitante.setNome("Internacional");
        visitante.setSiglaEstado(SiglaEstado.RS);
        visitante.setDataCriacao(LocalDate.of(1909, 4, 4));
        visitante.setAtivo(true);

        // Configuração do estádio
        estadio = new Estadio();
        estadio.setId(ESTADIO_ID);
        estadio.setNome("Arena do Grêmio");
        
        // Configuração dos DTOs de resposta
        mandanteResponseDTO = new ClubeResponseDTO();
        mandanteResponseDTO.setId(CLUBE_MANDANTE_ID);
        mandanteResponseDTO.setNome("Grêmio");
        
        visitanteResponseDTO = new ClubeResponseDTO();
        visitanteResponseDTO.setId(CLUBE_VISITANTE_ID);
        visitanteResponseDTO.setNome("Internacional");
        
        estadioResponseDTO = new EstadioResponseDTO();
        estadioResponseDTO.setId(ESTADIO_ID);
        estadioResponseDTO.setNome("Arena do Grêmio");

        // Configuração da partida
        partida = Partida.criar(mandante, visitante, estadio, DATA_HORA, GOLS_MANDANTE, GOLS_VISITANTE);
        partida.setId(PARTIDA_ID);

        // Configuração do DTO de requisição
        requestDTO = new PartidaRequestDTO();
        requestDTO.setMandanteId(CLUBE_MANDANTE_ID);
        requestDTO.setVisitanteId(CLUBE_VISITANTE_ID);
        requestDTO.setEstadioId(ESTADIO_ID);
        requestDTO.setDataHora(DATA_HORA);
        requestDTO.setGolsMandante(GOLS_MANDANTE);
        requestDTO.setGolsVisitante(GOLS_VISITANTE);

        // Configuração do DTO de resposta
        responseDTO = new PartidaResponseDTO();
        responseDTO.setId(PARTIDA_ID);
        responseDTO.setMandante(mandanteResponseDTO);
        responseDTO.setVisitante(visitanteResponseDTO);
        responseDTO.setEstadio(estadioResponseDTO);
        responseDTO.setDataHora(DATA_HORA);
        responseDTO.setGolsMandante(GOLS_MANDANTE);
        responseDTO.setGolsVisitante(GOLS_VISITANTE);
    }

    @Test
    void criarPartida_ComDadosValidos_DeveRetornarPartidaCriada() {
        // Arrange
        when(clubeUseCases.validarExistenciaClube(CLUBE_MANDANTE_ID)).thenReturn(mandante);
        when(clubeUseCases.validarExistenciaClube(CLUBE_VISITANTE_ID)).thenReturn(visitante);
        when(estadioUseCases.validarExistenciaEstadio(ESTADIO_ID)).thenReturn(estadio);
        when(partidaRepository.findAllByClube(any(Clube.class))).thenReturn(Collections.emptyList());
        when(partidaRepository.findAllByEstadioAndData(any(Estadio.class), any(LocalDate.class))).thenReturn(Collections.emptyList());
        when(partidaRepository.save(any(Partida.class))).thenReturn(partida);
        when(partidaMapper.EntityToDto(any(Partida.class))).thenReturn(responseDTO);

        // Act
        PartidaResponseDTO resultado = partidaService.criarPartida(requestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(PARTIDA_ID, resultado.getId());
        assertNotNull(resultado.getMandante());
        assertEquals(CLUBE_MANDANTE_ID, resultado.getMandante().getId());
        assertNotNull(resultado.getVisitante());
        assertEquals(CLUBE_VISITANTE_ID, resultado.getVisitante().getId());
        assertNotNull(resultado.getEstadio());
        assertEquals(ESTADIO_ID, resultado.getEstadio().getId());
        assertEquals(GOLS_MANDANTE, resultado.getGolsMandante());
        assertEquals(GOLS_VISITANTE, resultado.getGolsVisitante());
        
        verify(partidaRepository).save(any(Partida.class));
    }

    @Test
    void criarPartida_ComEstadioOcupado_DeveLancarExcecao() {
        // Arrange
        when(clubeUseCases.validarExistenciaClube(CLUBE_MANDANTE_ID)).thenReturn(mandante);
        when(clubeUseCases.validarExistenciaClube(CLUBE_VISITANTE_ID)).thenReturn(visitante);
        when(estadioUseCases.validarExistenciaEstadio(ESTADIO_ID)).thenReturn(estadio);
        when(partidaRepository.findAllByClube(any(Clube.class))).thenReturn(Collections.emptyList());
        when(partidaRepository.findAllByEstadioAndData(any(Estadio.class), any(LocalDate.class)))
            .thenReturn(List.of(new Partida())); // Estádio ocupado

        // Act & Assert
        assertThrows(EntidadeEmUsoException.class, 
            () -> partidaService.criarPartida(requestDTO));
            
        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void obterPartidaPorId_ComIdExistente_DeveRetornarPartida() {
        // Arrange
        when(partidaRepository.findById(PARTIDA_ID)).thenReturn(Optional.of(partida));
        when(partidaMapper.EntityToDto(any(Partida.class))).thenReturn(responseDTO);

        // Act
        PartidaResponseDTO resultado = partidaService.obterPartidaPorId(PARTIDA_ID);

        // Assert
        assertNotNull(resultado);
        assertEquals(PARTIDA_ID, resultado.getId());
    }

    @Test
    void obterPartidaPorId_ComIdInexistente_DeveLancarExcecao() {
        // Arrange
        when(partidaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntidadeNaoEncontradaException.class, 
            () -> partidaService.obterPartidaPorId(999L));
    }

    @Test
    void obterPartidas_ComFiltros_DeveRetornarPaginaDePartidas() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dataHora"));
        Page<Partida> page = new PageImpl<>(List.of(partida), pageable, 1);
        
        when(clubeUseCases.validarExistenciaClube(CLUBE_MANDANTE_ID)).thenReturn(mandante);
        when(estadioUseCases.validarExistenciaEstadio(ESTADIO_ID)).thenReturn(estadio);
        
        // Usando eq() para corresponder exatamente aos valores nulos e não nulos
        when(partidaRepository.findPartidasWithFilters(
            eq(mandante), // clube não nulo
            eq(estadio),  // estadio não nulo
            isNull(),     // goleada nulo
            isNull(),     // papel nulo
            eq(pageable)  // pageable não nulo
        )).thenReturn(page);
        
        when(partidaMapper.EntityToDto(any(Partida.class))).thenReturn(responseDTO);

        // Act
        var resultado = partidaService.obterPartidas(CLUBE_MANDANTE_ID, ESTADIO_ID, null, null, pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertNotNull(resultado.getContent().get(0));
        assertEquals(PARTIDA_ID, resultado.getContent().get(0).getId());
        
        // Verificações adicionais
        verify(clubeUseCases).validarExistenciaClube(CLUBE_MANDANTE_ID);
        verify(estadioUseCases).validarExistenciaEstadio(ESTADIO_ID);
        verify(partidaRepository).findPartidasWithFilters(
            eq(mandante),
            eq(estadio),
            isNull(),
            isNull(),
            eq(pageable)
        );
    }

    @Test
    void atualizarPartida_ComDadosValidos_DeveRetornarPartidaAtualizada() {
        // Arrange
        PartidaRequestDTO dtoAtualizado = new PartidaRequestDTO();
        dtoAtualizado.setMandanteId(CLUBE_MANDANTE_ID);
        dtoAtualizado.setVisitanteId(CLUBE_VISITANTE_ID);
        dtoAtualizado.setEstadioId(ESTADIO_ID);
        dtoAtualizado.setDataHora(DATA_HORA.plusDays(1));
        dtoAtualizado.setGolsMandante(3);
        dtoAtualizado.setGolsVisitante(2);

        PartidaResponseDTO responseAtualizado = new PartidaResponseDTO();
        responseAtualizado.setId(PARTIDA_ID);
        responseAtualizado.setMandante(mandanteResponseDTO);
        responseAtualizado.setVisitante(visitanteResponseDTO);
        responseAtualizado.setEstadio(estadioResponseDTO);
        responseAtualizado.setDataHora(DATA_HORA.plusDays(1));
        responseAtualizado.setGolsMandante(3);
        responseAtualizado.setGolsVisitante(2);

        when(partidaRepository.findById(PARTIDA_ID)).thenReturn(Optional.of(partida));
        when(clubeUseCases.validarExistenciaClube(CLUBE_MANDANTE_ID)).thenReturn(mandante);
        when(clubeUseCases.validarExistenciaClube(CLUBE_VISITANTE_ID)).thenReturn(visitante);
        when(estadioUseCases.validarExistenciaEstadio(ESTADIO_ID)).thenReturn(estadio);
        when(partidaRepository.findAllByClube(any(Clube.class))).thenReturn(Collections.emptyList());
        when(partidaRepository.findAllByEstadioAndData(any(Estadio.class), any(LocalDate.class))).thenReturn(Collections.emptyList());
        when(partidaRepository.save(any(Partida.class))).thenReturn(partida);
        when(partidaMapper.EntityToDto(any(Partida.class))).thenReturn(responseAtualizado);

        // Act
        PartidaResponseDTO resultado = partidaService.atualizarPartida(PARTIDA_ID, dtoAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.getGolsMandante());
        assertEquals(2, resultado.getGolsVisitante());
        verify(partidaRepository).save(any(Partida.class));
    }

    @Test
    void deletarPartida_ComIdExistente_DeveDeletarPartida() {
        // Arrange
        when(partidaRepository.findById(PARTIDA_ID)).thenReturn(Optional.of(partida));
        doNothing().when(partidaRepository).deletePartida(PARTIDA_ID);

        // Act
        partidaService.deletarPartida(PARTIDA_ID);

        // Assert
        verify(partidaRepository).deletePartida(PARTIDA_ID);
    }

    @Test
    void solicitarCriacaoPartida_DeveChamarMetodoDeNotificacao() {
        // Arrange
        doNothing().when(partidaEventsPort).notificarCriacaoPartida(any(PartidaRequestDTO.class));

        // Act
        partidaService.solicitarCriacaoPartida(requestDTO);

        // Assert
        verify(partidaEventsPort).notificarCriacaoPartida(requestDTO);
    }

    @Test
    void solicitarAtualizacaoPartida_DeveChamarMetodoDeNotificacao() {
        // Arrange
        doNothing().when(partidaEventsPort).notificarAtualizacaoPartida(anyLong(), any(PartidaRequestDTO.class));

        // Act
        partidaService.solicitarAtualizacaoPartida(PARTIDA_ID, requestDTO);

        // Assert
        verify(partidaEventsPort).notificarAtualizacaoPartida(PARTIDA_ID, requestDTO);
    }

    @Test
    void solicitarExclusaoPartida_DeveChamarMetodoDeNotificacao() {
        // Arrange
        doNothing().when(partidaEventsPort).notificarExclusaoPartida(anyLong());

        // Act
        partidaService.solicitarExclusaoPartida(PARTIDA_ID);

        // Assert
        verify(partidaEventsPort).notificarExclusaoPartida(PARTIDA_ID);
    }
}
