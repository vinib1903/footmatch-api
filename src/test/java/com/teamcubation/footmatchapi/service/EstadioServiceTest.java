package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.mapper.EstadioMapper;
import com.teamcubation.footmatchapi.repository.EstadioRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class EstadioServiceTest {

    @Mock
    private EstadioRepository estadioRepository;
    @Mock
    EstadioMapper estadioMapper;

    @InjectMocks
    EstadioService estadioService;

    @Test
    void testCreateStadiumWhenDataIsValid() {

        EstadioRequestDTO dto = EstadioRequestDTO.builder()
                .nome("Olímpico")
                .build();

        Estadio estadio = Estadio.builder()
                .id(1L)
                .nome(dto.getNome())
                .build();

        EstadioResponseDTO response = EstadioResponseDTO.builder()
                .id(1L)
                .nome(estadio.getNome())
                .build();

        when(estadioMapper.toDto(estadio))
                .thenReturn(response);

        when(estadioMapper.toEntity(dto))
                .thenReturn(estadio);

        when(estadioRepository.save(estadio))
                .thenReturn(estadio);

        EstadioResponseDTO result = estadioService.criarEstadio(dto);

        log.info("Response: {}", response);
        log.info("Result: {}", result);

        assertEquals(response, result);

        verify(estadioMapper, times(1)).toEntity(dto);
    }

    @Test
    void testCreateStadiumWhenNameAlreadyExists() {

        EstadioRequestDTO dto = EstadioRequestDTO.builder()
                .nome("Olímpico")
                .build();

        Estadio estadio = Estadio.builder()
                .id(1L)
                .nome(dto.getNome())
                .build();

        when(estadioRepository.findByNome(dto.getNome()))
                .thenReturn(Optional.of(estadio));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> estadioService.criarEstadio(dto));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());

        verify(estadioMapper, times(0)).toEntity(dto);
    }

    @Test
    void testCreateStadiumWhenDataIsInvalid() {

        EstadioRequestDTO dto = EstadioRequestDTO.builder()
                .nome("")
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<EstadioRequestDTO>> violations = validator.validate(dto);

        log.info("Violations: {}", violations);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("O nome deve conter ao menos 3 caracteres.")));

        verify(estadioMapper, times(0)).toEntity(dto);
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

        when(estadioRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(estadios));

        when(estadioMapper.toDto(estadio1))
                .thenReturn(dto1);

        when(estadioMapper.toDto(estadio2))
                .thenReturn(dto2);

        when(estadioMapper.toDto(estadio3))
                .thenReturn(dto3);

        when(estadioMapper.toDto(estadio4))
                .thenReturn(dto4);

        Page<EstadioResponseDTO> result = estadioService.obterEstadios(any(String.class), any(Pageable.class));

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

        when(estadioRepository.findById(1L))
                .thenReturn(Optional.of(estadio));

        when(estadioMapper.toDto(estadio))
                .thenReturn(dto);

        EstadioResponseDTO result = estadioService.obterEstadioPorId(1L);

        log.info("Response: {}", dto);
        log.info("Result: {}", result);

        assertEquals(dto, result);

        verify(estadioMapper, times(1)).toDto(estadio);
    }

    @Test
    void testGetStadiumByIdWhenDataIsInvalid() {

        when(estadioRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> estadioService.obterEstadioPorId(1L));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());

        verify(estadioRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateStadiumWhenDataIsValid() {

        Estadio estadio = Estadio.builder()
                .id(1L)
                .nome("Olímpico")
                .build();

        EstadioRequestDTO dto = EstadioRequestDTO.builder()
                .nome("Olímpico Monumental")
                .build();

        EstadioResponseDTO response = EstadioResponseDTO.builder()
                .id(1L)
                .nome(estadio.getNome())
                .build();

        when(estadioRepository.findById(1L))
                .thenReturn(Optional.of(estadio));

        when(estadioMapper.toDto(estadio))
                .thenReturn(response);

        when(estadioRepository.save(estadio))
                .thenReturn(estadio);

        EstadioResponseDTO result = estadioService.atualizarEstadio(1L, dto);

        log.info("Response: {}", response);
        log.info("Result: {}", result);

        assertEquals(response, result);

        verify(estadioMapper, times(1)).toDto(estadio);
        verify(estadioRepository, times(1)).save(estadio);
    }

    @Test
    void testUpdateStadiumWhenEstadioIdIsInvalid() {

        EstadioRequestDTO dto = EstadioRequestDTO.builder()
                .nome("Ol")
                .build();

        when(estadioRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> estadioService.atualizarEstadio(1L, dto));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());

        verify(estadioRepository, times(1)).findById(1L);
    }
}
