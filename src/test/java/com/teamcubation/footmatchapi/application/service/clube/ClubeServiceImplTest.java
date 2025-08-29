package com.teamcubation.footmatchapi.application.service.clube;

import com.teamcubation.footmatchapi.application.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.application.ports.out.ClubeEventsPort;
import com.teamcubation.footmatchapi.application.ports.out.ClubeRepository;
import com.teamcubation.footmatchapi.application.ports.out.PartidaRepository;
import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.domain.exceptions.EntidadeEmUsoException;
import com.teamcubation.footmatchapi.domain.exceptions.EntidadeNaoEncontradaException;
import com.teamcubation.footmatchapi.utils.mapper.ClubeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClubeServiceImplTest {

    @Mock
    private ClubeRepository clubeRepository;

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private ClubeMapper clubeMapper;

    @Mock
    private ClubeEventsPort clubeEventsPort;

    @InjectMocks
    private ClubeServiceImpl clubeService;

    private Clube clube;
    private ClubeRequestDTO requestDTO;
    private ClubeResponseDTO responseDTO;
    private static final Long CLUBE_ID = 1L;
    private static final String NOME_CLUBE = "Grêmio";
    private static final String SIGLA_ESTADO = "RS";
    private static final LocalDate DATA_CRIACAO = LocalDate.of(1903, 9, 15);

    @BeforeEach
    void setUp() {
        // Configuração do Clube
        clube = new Clube();
        clube.setId(CLUBE_ID);
        clube.setNome(NOME_CLUBE);
        clube.setSiglaEstado(SiglaEstado.RS);
        clube.setDataCriacao(DATA_CRIACAO);
        clube.setAtivo(true);

        // Configuração do DTO de requisição
        requestDTO = new ClubeRequestDTO();
        requestDTO.setNome(NOME_CLUBE);
        requestDTO.setSiglaEstado(SIGLA_ESTADO);
        requestDTO.setDataCriacao(DATA_CRIACAO);
        requestDTO.setAtivo(true);

        // Configuração do DTO de resposta
        responseDTO = new ClubeResponseDTO();
        responseDTO.setId(CLUBE_ID);
        responseDTO.setNome(NOME_CLUBE);
        responseDTO.setSiglaEstado(SIGLA_ESTADO);
        responseDTO.setDataCriacao(DATA_CRIACAO);
        responseDTO.setAtivo(true);
    }

    @Test
    void criarClube_ComDadosValidos_DeveRetornarClubeCriado() {
        // Arrange
        when(clubeRepository.findByNomeAndSiglaEstado(anyString(), any(SiglaEstado.class))).thenReturn(null);
        when(clubeMapper.entityToDto(any(Clube.class))).thenReturn(responseDTO);
        when(clubeRepository.save(any(Clube.class))).thenReturn(clube);

        // Act
        ClubeResponseDTO resultado = clubeService.criarClube(requestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(CLUBE_ID, resultado.getId());
        assertEquals(NOME_CLUBE, resultado.getNome());
        assertEquals(SIGLA_ESTADO, resultado.getSiglaEstado());
        assertTrue(resultado.getAtivo());
        
        verify(clubeRepository).save(any(Clube.class));
    }

    @Test
    void criarClube_ComNomeExistenteNoEstado_DeveLancarExcecao() {
        // Arrange
        when(clubeRepository.findByNomeAndSiglaEstado(anyString(), any(SiglaEstado.class))).thenReturn(clube);

        // Act & Assert
        assertThrows(EntidadeEmUsoException.class, 
            () -> clubeService.criarClube(requestDTO));
            
        verify(clubeRepository, never()).save(any(Clube.class));
    }

    @Test
    void obterClubePorId_ComIdExistente_DeveRetornarClube() {
        // Arrange
        when(clubeRepository.findById(CLUBE_ID)).thenReturn(Optional.of(clube));
        when(clubeMapper.entityToDto(any(Clube.class))).thenReturn(responseDTO);

        // Act
        ClubeResponseDTO resultado = clubeService.obterClubePorId(CLUBE_ID);

        // Assert
        assertNotNull(resultado);
        assertEquals(CLUBE_ID, resultado.getId());
        assertEquals(NOME_CLUBE, resultado.getNome());
    }

    @Test
    void obterClubePorId_ComIdInexistente_DeveLancarExcecao() {
        // Arrange
        when(clubeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntidadeNaoEncontradaException.class, 
            () -> clubeService.obterClubePorId(999L));
    }

    @Test
    void obterClubes_ComFiltros_DeveRetornarPaginaDeClubes() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome"));
        Page<Clube> page = new PageImpl<>(Collections.singletonList(clube), pageable, 1);
        
        when(clubeRepository.findClubesWithFilters(anyString(), any(), anyBoolean(), any(Pageable.class)))
            .thenReturn(page);
        when(clubeMapper.entityToDto(any(Clube.class))).thenReturn(responseDTO);

        // Act
        var resultado = clubeService.obterClubes("Grêmio", "RS", true, pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(NOME_CLUBE, resultado.getContent().get(0).getNome());
    }

    @Test
    void atualizarClube_ComDadosValidos_DeveRetornarClubeAtualizado() {
        // Arrange
        ClubeRequestDTO dtoAtualizado = new ClubeRequestDTO();
        dtoAtualizado.setNome("Grêmio Foot-Ball Porto Alegrense");
        dtoAtualizado.setSiglaEstado("RS");
        dtoAtualizado.setDataCriacao(DATA_CRIACAO);
        dtoAtualizado.setAtivo(true);

        ClubeResponseDTO responseAtualizado = new ClubeResponseDTO();
        responseAtualizado.setId(CLUBE_ID);
        responseAtualizado.setNome("Grêmio Foot-Ball Porto Alegrense");
        responseAtualizado.setSiglaEstado("RS");
        responseAtualizado.setDataCriacao(DATA_CRIACAO);
        responseAtualizado.setAtivo(true);

        when(clubeRepository.findById(CLUBE_ID)).thenReturn(Optional.of(clube));
        when(clubeRepository.findByNomeAndSiglaEstado(anyString(), any(SiglaEstado.class))).thenReturn(null);
        when(clubeRepository.save(any(Clube.class))).thenReturn(clube);
        when(clubeMapper.entityToDto(any(Clube.class))).thenReturn(responseAtualizado);

        // Act
        ClubeResponseDTO resultado = clubeService.atualizarClube(CLUBE_ID, dtoAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Grêmio Foot-Ball Porto Alegrense", resultado.getNome());
        verify(clubeRepository).save(any(Clube.class));
    }

    @Test
    void inativarClube_ComIdExistente_DeveInativarClube() {
        // Arrange
        when(clubeRepository.findById(CLUBE_ID)).thenReturn(Optional.of(clube));
        when(clubeRepository.save(any(Clube.class))).thenReturn(clube);

        // Act
        clubeService.inativarClube(CLUBE_ID);

        // Assert
        assertFalse(clube.getAtivo());
        verify(clubeRepository).save(clube);
    }

    @Test
    void inativarClube_ComIdInexistente_DeveLancarExcecao() {
        // Arrange
        when(clubeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntidadeNaoEncontradaException.class, 
            () -> clubeService.inativarClube(999L));
            
        verify(clubeRepository, never()).save(any(Clube.class));
    }

    @Test
    void solicitarCriacaoClube_DeveChamarMetodoDeNotificacao() {
        // Arrange
        doNothing().when(clubeEventsPort).notificarCriacaoClube(any(ClubeRequestDTO.class));

        // Act
        clubeService.solicitarCriacaoClube(requestDTO);

        // Assert
        verify(clubeEventsPort).notificarCriacaoClube(requestDTO);
    }

    @Test
    void solicitarAtualizacaoClube_DeveChamarMetodoDeNotificacao() {
        // Arrange
        doNothing().when(clubeEventsPort).notificarAtualizacaoClube(anyLong(), any(ClubeRequestDTO.class));

        // Act
        clubeService.solicitarAtualizacaoClube(CLUBE_ID, requestDTO);

        // Assert
        verify(clubeEventsPort).notificarAtualizacaoClube(CLUBE_ID, requestDTO);
    }

    @Test
    void solicitarInativacaoClube_DeveChamarMetodoDeNotificacao() {
        // Arrange
        doNothing().when(clubeEventsPort).notificarInativacaoClube(anyLong());

        // Act
        clubeService.solicitarInativacaoClube(CLUBE_ID);

        // Assert
        verify(clubeEventsPort).notificarInativacaoClube(CLUBE_ID);
    }
}
