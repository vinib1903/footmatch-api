package com.teamcubation.footmatchapi.application.service.estadio;

import com.teamcubation.footmatchapi.application.ports.out.EstadioEventsPort;
import com.teamcubation.footmatchapi.application.ports.out.EstadioRepository;
import com.teamcubation.footmatchapi.application.ports.out.ViacepClientPort;
import com.teamcubation.footmatchapi.domain.entities.Endereco;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.application.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.ViaCepResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.EnderecoResponseDTO;
import com.teamcubation.footmatchapi.domain.exceptions.EntidadeEmUsoException;
import com.teamcubation.footmatchapi.domain.exceptions.EntidadeNaoEncontradaException;
import com.teamcubation.footmatchapi.utils.mapper.EnderecoMapper;
import com.teamcubation.footmatchapi.utils.mapper.EstadioMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadioServiceImplTest {

    @Mock
    private EstadioRepository estadioRepository;

    @Mock
    private EstadioMapper estadioMapper;

    @Mock
    private EnderecoMapper enderecoMapper;

    @Mock
    private ViacepClientPort viacepClientPort;

    @Mock
    private EstadioEventsPort estadioEventsPort;

    @InjectMocks
    private EstadioServiceImpl estadioService;

    private Estadio estadio;
    private EstadioRequestDTO requestDTO;
    private EstadioResponseDTO responseDTO;
    private ViaCepResponseDTO viaCepResponseDTO;
    private Endereco endereco;
    private EnderecoResponseDTO enderecoResponseDTO;

    @BeforeEach
    void setUp() {
        // Configuração dos objetos de teste
        viaCepResponseDTO = new ViaCepResponseDTO();
        viaCepResponseDTO.setCep("01001000");
        viaCepResponseDTO.setLogradouro("Praça da Sé");
        viaCepResponseDTO.setLocalidade("São Paulo");
        viaCepResponseDTO.setUf("SP");

        endereco = new Endereco();
        endereco.setCep("01001000");
        endereco.setLogradouro("Praça da Sé");
        endereco.setLocalidade("São Paulo");
        endereco.setUf("SP");

        estadio = new Estadio();
        estadio.setId(1L);
        estadio.setNome("Estádio do Morumbi");
        estadio.setEndereco(endereco);

        requestDTO = new EstadioRequestDTO();
        requestDTO.setNome("Estádio do Morumbi");
        requestDTO.setCep("01001000");

        enderecoResponseDTO = new EnderecoResponseDTO();
        enderecoResponseDTO.setCep("01001000");
        enderecoResponseDTO.setLocalidade("São Paulo");
        enderecoResponseDTO.setUf("SP");
        enderecoResponseDTO.setLogradouro("Praça da Sé");
        
        responseDTO = new EstadioResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNome("Estádio do Morumbi");
        responseDTO.setEndereco(enderecoResponseDTO);
    }

    @Test
    void criarEstadio_ComDadosValidos_DeveRetornarEstadioCriado() {
        // Arrange
        when(estadioRepository.findByNome(anyString())).thenReturn(Optional.empty());
        when(estadioRepository.findByEndereco_Cep(anyString())).thenReturn(Optional.empty());
        when(viacepClientPort.buscarEnderecoPorCep(anyString())).thenReturn(viaCepResponseDTO);
        when(enderecoMapper.fromViaCepResponse(any())).thenReturn(endereco);
        when(estadioMapper.entityToDto(any(Estadio.class))).thenReturn(responseDTO);
        when(estadioRepository.save(any(Estadio.class))).thenReturn(estadio);
        doNothing().when(estadioEventsPort).notificarCriacaoEstadio(any(EstadioRequestDTO.class));

        // Act
        EstadioResponseDTO resultado = estadioService.criarEstadio(requestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Estádio do Morumbi", resultado.getNome());
        assertNotNull(resultado.getEndereco());
        assertEquals("01001000", resultado.getEndereco().getCep());
        
        verify(estadioRepository).save(any(Estadio.class));
        verify(estadioEventsPort).notificarCriacaoEstadio(any(EstadioRequestDTO.class));
    }

    @Test
    void criarEstadio_ComNomeExistente_DeveLancarExcecao() {
        // Arrange
        when(estadioRepository.findByNome(anyString())).thenReturn(Optional.of(estadio));

        // Act & Assert
        assertThrows(EntidadeEmUsoException.class, 
            () -> estadioService.criarEstadio(requestDTO));
            
        verify(estadioRepository, never()).save(any(Estadio.class));
    }

    @Test
    void obterEstadioPorId_ComIdExistente_DeveRetornarEstadio() {
        // Arrange
        when(estadioRepository.findById(1L)).thenReturn(Optional.of(estadio));
        when(estadioMapper.entityToDto(any(Estadio.class))).thenReturn(responseDTO);

        // Act
        EstadioResponseDTO resultado = estadioService.obterEstadioPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Estádio do Morumbi", resultado.getNome());
        assertNotNull(resultado.getEndereco());
        assertEquals("01001000", resultado.getEndereco().getCep());
    }

    @Test
    void obterEstadioPorId_ComIdInexistente_DeveLancarExcecao() {
        // Arrange
        when(estadioRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntidadeNaoEncontradaException.class, 
            () -> estadioService.obterEstadioPorId(999L));
    }

    @Test
    void obterEstadios_DeveRetornarPaginaDeEstadios() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome"));
        Page<Estadio> page = new PageImpl<>(Collections.singletonList(estadio), pageable, 1);
        
        when(estadioRepository.findStadiumsWichFilters(anyString(), any(Pageable.class))).thenReturn(page);
        when(estadioMapper.entityToDto(any(Estadio.class))).thenReturn(responseDTO);

        // Act
        var resultado = estadioService.obterEstadios("Morumbi", pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Estádio do Morumbi", resultado.getContent().get(0).getNome());
    }

    @Test
    void atualizarEstadio_ComDadosValidos_DeveRetornarEstadioAtualizado() {
        // Arrange
        when(estadioRepository.findById(1L)).thenReturn(Optional.of(estadio));
        when(estadioRepository.findByNome(anyString())).thenReturn(Optional.empty());
        when(estadioRepository.findByEndereco_Cep(anyString())).thenReturn(Optional.of(estadio));
        when(viacepClientPort.buscarEnderecoPorCep(anyString())).thenReturn(viaCepResponseDTO);
        when(enderecoMapper.fromViaCepResponse(any())).thenReturn(endereco);
        when(estadioRepository.save(any(Estadio.class))).thenReturn(estadio);
        when(estadioMapper.entityToDto(any(Estadio.class))).thenReturn(responseDTO);
        doNothing().when(estadioEventsPort).notificarAtualizacaoEstadio(anyLong(), any(EstadioRequestDTO.class));

        // Act
        EstadioResponseDTO resultado = estadioService.atualizarEstadio(1L, requestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Estádio do Morumbi", resultado.getNome());
        
        verify(estadioRepository).save(any(Estadio.class));
        verify(estadioEventsPort).notificarAtualizacaoEstadio(1L, requestDTO);
    }

    @Test
    void validarExistenciaEstadio_ComIdInexistente_DeveLancarExcecao() {
        // Arrange
        when(estadioRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntidadeNaoEncontradaException.class, 
            () -> estadioService.validarExistenciaEstadio(999L));
    }
}
