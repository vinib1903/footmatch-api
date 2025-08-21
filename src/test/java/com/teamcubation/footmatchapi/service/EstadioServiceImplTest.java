package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.entities.Endereco;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.application.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.ViaCepResponseDTO;
import com.teamcubation.footmatchapi.adapters.outbound.integration.ViacepClient;
import com.teamcubation.footmatchapi.utils.mapper.EnderecoMapper;
import com.teamcubation.footmatchapi.utils.mapper.EstadioMapper;
import com.teamcubation.footmatchapi.adapters.outbound.repository.EstadioJpaRepository;
import com.teamcubation.footmatchapi.application.service.estadio.EstadioServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class EstadioServiceImplTest {

    @Mock
    private EstadioJpaRepository estadioJpaRepository;
    @Mock
    EstadioMapper estadioMapper;
    @Mock
    EnderecoMapper enderecoMapper;
    @Mock
    ViacepClient viacepClient;

    @InjectMocks
    EstadioServiceImpl estadioServiceImpl;

    @Test
    void testCreateStadiumWhenDataIsValid() {

        EstadioRequestDTO dto = EstadioRequestDTO.builder()
                .nome("Olímpico")
                .cep("93300000")
                .build();

        ViaCepResponseDTO viaCepResponse = ViaCepResponseDTO.builder()
                .cep("93300-000")
                .logradouro("Rua Sarandi")
                .bairro("Jardim Mauá")
                .localidade("Novo Hamburgo")
                .uf("RS")
                .erro(false)
                .build();

        Endereco endereco = Endereco.builder()
                .cep("93300000")
                .logradouro("Rua Sarandi")
                .bairro("Jardim Mauá")
                .localidade("Novo Hamburgo")
                .uf("RS")
                .build();

        Estadio estadioEntity = Estadio.builder()
                .nome(dto.getNome())
                .endereco(endereco)
                .build();

        Estadio estadioSalvo = Estadio.builder()
                .id(1L)
                .nome(dto.getNome())
                .endereco(endereco)
                .build();

        EstadioResponseDTO response = EstadioResponseDTO.builder()
                .id(1L)
                .nome(estadioSalvo.getNome())
                .build();

        when(estadioJpaRepository.findByNome(dto.getNome()))
                .thenReturn(Optional.empty());
        when(estadioJpaRepository.findByEndereco_Cep("93300000"))
                .thenReturn(Optional.empty());
        when(viacepClient.buscarEnderecoPorCep("93300000"))
                .thenReturn(viaCepResponse);
        when(enderecoMapper.fromViaCepResponse(viaCepResponse))
                .thenReturn(endereco);
        when(estadioMapper.toEntity(dto))
                .thenReturn(estadioEntity);
        when(estadioJpaRepository.save(any(Estadio.class)))
                .thenReturn(estadioSalvo);
        when(estadioMapper.toDto(any(Estadio.class)))
                .thenReturn(response);

        EstadioResponseDTO result = estadioServiceImpl.criarEstadio(dto);

        log.info("Response: {}", response);
        log.info("Result: {}", result);

        assertEquals(response, result);

        verify(estadioMapper, times(1)).toEntity(dto);
        verify(estadioJpaRepository, times(1)).save(any(Estadio.class));
        verify(viacepClient, times(1)).buscarEnderecoPorCep("93300000");
    }

    @Test
    void testCreateStadiumWhenNameAlreadyExists() {

        EstadioRequestDTO dto = EstadioRequestDTO.builder()
                .nome("Olímpico")
                .cep("93300000")
                .build();

        Estadio estadio = Estadio.builder()
                .id(1L)
                .nome(dto.getNome())
                .build();

        when(estadioJpaRepository.findByNome(dto.getNome()))
                .thenReturn(Optional.of(estadio));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> estadioServiceImpl.criarEstadio(dto));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());

        verify(estadioMapper, never()).toEntity(dto);
        verify(viacepClient, never()).buscarEnderecoPorCep(anyString());
    }

    @Test
    void testCreateStadiumWhenDataIsInvalid() {

        EstadioRequestDTO dto = EstadioRequestDTO.builder()
                .nome("")
                .cep("")
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<EstadioRequestDTO>> violations = validator.validate(dto);

        log.info("Violations: {}", violations);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("O nome deve conter ao menos 3 caracteres.") || v.getMessage().contains("O nome do estádio é obrigatório.")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("O CEP do estádio é obrigatório.") || v.getMessage().contains("O CEP deve conter 8 dígitos numéricos.")));

        verify(estadioMapper, never()).toEntity(dto);
    }

    @Test
    void testGetAllStadiumsOrderByNameAsc() {

        Estadio estadio1 = Estadio.builder()
                .id(1L)
                .nome("Olímpico")
                .build();

        Estadio estadio2 = Estadio.builder()
                .id(2L)
                .nome("Maracanã")
                .build();

        Estadio estadio3 = Estadio.builder()
                .id(3L)
                .nome("Mineirão")
                .build();

        Estadio estadio4 = Estadio.builder()
                .id(4L)
                .nome("Alfredo Jaconi")
                .build();

        EstadioResponseDTO dto1 = EstadioResponseDTO.builder()
                .id(1L)
                .nome(estadio1.getNome())
                .build();

        EstadioResponseDTO dto2 = EstadioResponseDTO.builder()
                .id(2L)
                .nome(estadio2.getNome())
                .build();

        EstadioResponseDTO dto3 = EstadioResponseDTO.builder()
                .id(3L)
                .nome(estadio3.getNome())
                .build();

        EstadioResponseDTO dto4 = EstadioResponseDTO.builder()
                .id(4L)
                .nome(estadio4.getNome())
                .build();

        List<Estadio> estadios = List.of(estadio1, estadio2, estadio3, estadio4)
                .stream()
                .sorted(Comparator.comparing(Estadio::getNome))
                .toList();

        when(estadioJpaRepository.findStadiumsWichFilters(any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(estadios));

        when(estadioMapper.toDto(estadio1))
                .thenReturn(dto1);

        when(estadioMapper.toDto(estadio2))
                .thenReturn(dto2);

        when(estadioMapper.toDto(estadio3))
                .thenReturn(dto3);

        when(estadioMapper.toDto(estadio4))
                .thenReturn(dto4);

        Page<EstadioResponseDTO> result = estadioServiceImpl.obterEstadios(null, PageRequest.of(0, 10));

        log.info("Result: {}", result);

        assertEquals(4, result.getContent().size());
        assertEquals(List.of(dto4, dto2, dto3, dto1), result.getContent());
        assertEquals("Olímpico", result.getContent().get(3).getNome());
        assertEquals("Alfredo Jaconi", result.getContent().get(0).getNome());

        verify(estadioMapper, times(1)).toDto(estadio1);
        verify(estadioMapper, times(1)).toDto(estadio2);
        verify(estadioMapper, times(1)).toDto(estadio3);
        verify(estadioMapper, times(1)).toDto(estadio4);
    }

    @Test
    void testGetStadiumByIdWhenDataIsValid() {

        Estadio estadio = Estadio.builder()
                .id(1L)
                .nome("Olímpico")
                .build();

        EstadioResponseDTO dto = EstadioResponseDTO.builder()
                .id(1L)
                .nome(estadio.getNome())
                .build();

        when(estadioJpaRepository.findById(1L))
                .thenReturn(Optional.of(estadio));

        when(estadioMapper.toDto(estadio))
                .thenReturn(dto);

        EstadioResponseDTO result = estadioServiceImpl.obterEstadioPorId(1L);

        log.info("Response: {}", dto);
        log.info("Result: {}", result);

        assertEquals(dto, result);

        verify(estadioMapper, times(1)).toDto(estadio);
    }

    @Test
    void testGetStadiumByIdWhenDataIsInvalid() {

        when(estadioJpaRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> estadioServiceImpl.obterEstadioPorId(1L));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());

        verify(estadioJpaRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateStadiumWhenDataIsValid() {

        EstadioRequestDTO dto = EstadioRequestDTO.builder()
                .nome("Olímpico Monumental")
                .cep("93300000")
                .build();

        ViaCepResponseDTO viaCepResponse = ViaCepResponseDTO.builder()
                .cep("93300-000")
                .logradouro("Rua Sarandi")
                .bairro("Jardim Mauá")
                .localidade("Novo Hamburgo")
                .uf("RS")
                .erro(false)
                .build();

        Endereco endereco = Endereco.builder()
                .cep("93300000")
                .logradouro("Rua Sarandi")
                .bairro("Jardim Mauá")
                .localidade("Novo Hamburgo")
                .uf("RS")
                .build();

        Estadio estadio = Estadio.builder()
                .id(1L)
                .nome("Olímpico")
                .endereco(endereco)
                .build();

        EstadioResponseDTO response = EstadioResponseDTO.builder()
                .id(1L)
                .nome("Olímpico Monumental")
                .build();

        when(estadioJpaRepository.findById(1L))
                .thenReturn(Optional.of(estadio));
        when(estadioJpaRepository.findByNome(dto.getNome()))
                .thenReturn(Optional.empty());
        when(estadioJpaRepository.findByEndereco_Cep("93300000"))
                .thenReturn(Optional.of(estadio)); // Mesmo estádio, então pode usar o mesmo CEP
        when(viacepClient.buscarEnderecoPorCep("93300000"))
                .thenReturn(viaCepResponse);
        when(enderecoMapper.fromViaCepResponse(viaCepResponse))
                .thenReturn(endereco);
        when(estadioJpaRepository.save(any(Estadio.class)))
                .thenReturn(estadio);
        when(estadioMapper.toDto(any(Estadio.class)))
                .thenReturn(response);

        EstadioResponseDTO result = estadioServiceImpl.atualizarEstadio(1L, dto);

        log.info("Response: {}", response);
        log.info("Result: {}", result);

        assertEquals(response, result);

        verify(estadioMapper, times(1)).toDto(any(Estadio.class));
        verify(estadioJpaRepository, times(1)).save(any(Estadio.class));
        verify(viacepClient, times(1)).buscarEnderecoPorCep("93300000");
    }

    @Test
    void testUpdateStadiumWhenEstadioIdIsInvalid() {

        EstadioRequestDTO dto = EstadioRequestDTO.builder()
                .nome("Olímpico")
                .cep("93300000")
                .build();

        when(estadioJpaRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> estadioServiceImpl.atualizarEstadio(1L, dto));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());

        verify(estadioJpaRepository, times(1)).findById(1L);
        verify(viacepClient, never()).buscarEnderecoPorCep(anyString());
    }
}
