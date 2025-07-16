package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.mapper.ClubeMapper;
import com.teamcubation.footmatchapi.repository.ClubeRepository;
import com.teamcubation.footmatchapi.repository.PartidaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class ClubeServiceTest {

    @Mock
    private ClubeRepository clubeRepository;
    @Mock
    private PartidaRepository partidaRepository;
    @Mock
    private ClubeMapper clubeMapper;

    @InjectMocks
    private ClubeService clubeService;

    @Test
    void testCreateClubWhenDataIsValid() {

        ClubeRequestDTO dto = ClubeRequestDTO.builder()
                .nome("Grêmio")
                .siglaEstado("RS")
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Clube clube = Clube.builder().id(1L)
                .nome(dto.getNome())
                .siglaEstado(SiglaEstado.valueOf(dto.getSiglaEstado()))
                .dataCriacao(dto.getDataCriacao())
                .ativo(dto.getAtivo())
                .build();

        ClubeResponseDTO response = ClubeResponseDTO.builder()
                .id(1L).nome(clube.getNome())
                .siglaEstado(clube.getSiglaEstado().toString())
                .dataCriacao(clube.getDataCriacao())
                .ativo(clube.getAtivo())
                .build();

        when(clubeMapper.toEntity(dto))
                .thenReturn(clube);

        when(clubeRepository.save(clube))
                .thenReturn(clube);

        when(clubeMapper.toDto(clube))
                .thenReturn(response);

        ClubeResponseDTO result = clubeService.criarClube(dto);

        log.info("Response: {}", response);
        log.info("Result: {}", result);

        assertEquals(response, result);
    }

    @Test
    void testCreateClubWhenStateIsInvalid() {

        ClubeRequestDTO dto = ClubeRequestDTO.builder()
                .nome("Grêmio")
                .siglaEstado("POA")
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clubeService.criarClube(dto));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testCreateClubWhenCreationDateIsOnFuture() {
        ClubeRequestDTO dto = ClubeRequestDTO.builder()
                .nome("Grêmio")
                .siglaEstado("RS")
                .dataCriacao(LocalDate.now().plusDays(1))
                .ativo(true)
                .build();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clubeService.criarClube(dto));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testCreateClubWhenNameAlreadyExists() {

        ClubeRequestDTO dto = ClubeRequestDTO.builder()
                .nome("Grêmio")
                .siglaEstado("RS")
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Clube clube = Clube.builder()
                .id(7L)
                .nome(dto.getNome())
                .siglaEstado(SiglaEstado.valueOf(dto.getSiglaEstado()))
                .dataCriacao(LocalDate.of(1983, 12, 15))
                .ativo(false)
                .build();

        when(clubeRepository.findByNomeAndSiglaEstado(dto.getNome(), SiglaEstado.valueOf(dto.getSiglaEstado())))
                .thenReturn(Optional.of(clube));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clubeService.criarClube(dto));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void testGetAllClubsWhenOrderByNameDesc() {

        Clube clube1 = Clube.builder()
                .id(1L)
                .nome("Grêmio")
                .siglaEstado(SiglaEstado.valueOf("RS"))
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Clube clube2 = Clube.builder()
                .id(2L)
                .nome("Flamengo")
                .siglaEstado(SiglaEstado.valueOf("RJ"))
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Clube clube3 = Clube.builder()
                .id(3L)
                .nome("Palmeiras")
                .siglaEstado(SiglaEstado.valueOf("SP"))
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Clube clube4 = Clube.builder()
                .id(4L)
                .nome("Santos")
                .siglaEstado(SiglaEstado.valueOf("SP"))
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Clube clube5 = Clube.builder()
                .id(5L)
                .nome("Corinthians")
                .siglaEstado(SiglaEstado.valueOf("SP"))
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        Clube clube6 = Clube.builder()
                .id(6L)
                .nome("Bahia")
                .siglaEstado(SiglaEstado.valueOf("BA"))
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        ClubeResponseDTO dto1 = ClubeResponseDTO.builder()
                .id(1L)
                .nome(clube1.getNome())
                .siglaEstado(clube1.getSiglaEstado().toString())
                .dataCriacao(clube1.getDataCriacao())
                .ativo(clube1.getAtivo())
                .build();

        ClubeResponseDTO dto2 = ClubeResponseDTO.builder()
                .id(2L)
                .nome(clube2.getNome())
                .siglaEstado(clube2.getSiglaEstado().toString())
                .dataCriacao(clube2.getDataCriacao())
                .ativo(clube2.getAtivo())
                .build();

        ClubeResponseDTO dto3 = ClubeResponseDTO.builder()
                .id(3L)
                .nome(clube3.getNome())
                .siglaEstado(clube3.getSiglaEstado().toString())
                .dataCriacao(clube3.getDataCriacao())
                .ativo(clube3.getAtivo())
                .build();

        ClubeResponseDTO dto4 = ClubeResponseDTO.builder()
                .id(4L)
                .nome(clube4.getNome())
                .siglaEstado(clube4.getSiglaEstado().toString())
                .dataCriacao(clube4.getDataCriacao())
                .ativo(clube4.getAtivo())
                .build();

        ClubeResponseDTO dto5 = ClubeResponseDTO.builder()
                .id(5L)
                .nome(clube5.getNome())
                .siglaEstado(clube5.getSiglaEstado().toString())
                .dataCriacao(clube5.getDataCriacao())
                .ativo(clube5.getAtivo())
                .build();

        ClubeResponseDTO dto6 = ClubeResponseDTO.builder()
                .id(6L)
                .nome(clube6.getNome())
                .siglaEstado(clube6.getSiglaEstado().toString())
                .dataCriacao(clube6.getDataCriacao())
                .ativo(clube6.getAtivo())
                .build();

        List<Clube> clubes = List.of(clube1, clube2, clube3, clube4, clube5, clube6)
                .stream()
                .sorted(Comparator.comparing(Clube::getNome).reversed())
                .toList();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome").descending());
        Page<Clube> page = new PageImpl<>(clubes, pageable, clubes.size());

        when(clubeRepository.findClubesWichFilters(null,null,null, pageable))
                .thenReturn(page);

        when(clubeMapper.toDto(clube1))
                .thenReturn(dto1);

        when(clubeMapper.toDto(clube2))
                .thenReturn(dto2);

        when(clubeMapper.toDto(clube3))
                .thenReturn(dto3);

        when(clubeMapper.toDto(clube4))
                .thenReturn(dto4);

        when(clubeMapper.toDto(clube5))
                .thenReturn(dto5);

        when(clubeMapper.toDto(clube6))
                .thenReturn(dto6);

        Page<ClubeResponseDTO> result = clubeService.obterClubes(null, null, null, pageable);

        log.info("Result: {}", result);

        assertEquals(6, result.getTotalElements());
        assertEquals(List.of(dto4, dto3, dto1, dto2, dto5, dto6), result.getContent());
        assertEquals("Bahia", result.getContent().get(5).getNome());
        assertEquals("Grêmio", result.getContent().get(2).getNome());
    }

    @Test
    void testGetClubByIdWhenDataIsValid() {

        Clube clube = Clube.builder()
                .id(1L)
                .nome("Grêmio")
                .siglaEstado(SiglaEstado.valueOf("RS"))
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        ClubeResponseDTO dto = ClubeResponseDTO.builder()
                .id(1L)
                .nome(clube.getNome())
                .siglaEstado(clube.getSiglaEstado().toString())
                .dataCriacao(clube.getDataCriacao())
                .ativo(clube.getAtivo())
                .build();

        when(clubeRepository.findById(1L))
                .thenReturn(Optional.of(clube));

        when(clubeMapper.toDto(clube))
                .thenReturn(dto);

        ClubeResponseDTO result = clubeService.obterClubePorId(1L);

        log.info("Response: {}", dto);
        log.info("Result: {}", result);

        assertEquals(dto, result);

    }

    @Test
    void testGetClubByIdWhenDataIsInvalid() {

        when(clubeRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clubeService.obterClubePorId(1L));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void testUpdateClubWhenDataIsValid() {

        Clube clube = Clube.builder()
                .id(1L)
                .nome("Grêmio")
                .siglaEstado(SiglaEstado.valueOf("RS"))
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        ClubeRequestDTO dto = ClubeRequestDTO.builder()
                .nome("Grêmio FBPA")
                .siglaEstado("RS")
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        ClubeResponseDTO responseDto = ClubeResponseDTO.builder()
                .id(1L)
                .nome(clube.getNome())
                .siglaEstado(clube.getSiglaEstado().toString())
                .dataCriacao(clube.getDataCriacao())
                .ativo(clube.getAtivo())
                .build();

        when(clubeRepository.findById(1L))
                .thenReturn(Optional.of(clube));

        when(clubeRepository.findByNomeAndSiglaEstado(dto.getNome(), SiglaEstado.valueOf(dto.getSiglaEstado())))
                .thenReturn(Optional.empty());

        when(clubeRepository.save(clube))
                .thenReturn(clube);

        when(clubeMapper.toDto(clube))
                .thenReturn(responseDto);

        ClubeResponseDTO result = clubeService.atualizarClube(1L, dto);

        log.info("Response: {}", responseDto);
        log.info("Result: {}", result);

        assertEquals(responseDto, result);
    }

    @Test
    void testUpdateClubWhenCreationIsAfterMatchAlreadyExists() {

        Clube clube = Clube.builder()
                .id(1L).nome("Grêmio")
                .siglaEstado(SiglaEstado.valueOf("RS"))
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        ClubeRequestDTO dto = ClubeRequestDTO.builder()
                .nome("Grêmio FBPA")
                .siglaEstado("RS")
                .dataCriacao(LocalDate.of(2022, 9, 15))
                .ativo(true)
                .build();

        Partida partida = mock(Partida.class);

        when(partida.getDataHora())
                .thenReturn(LocalDate.of(2021, 9, 15).atStartOfDay());

        when(clubeRepository.findById(1L))
                .thenReturn(Optional.of(clube));

        when(clubeRepository.findByNomeAndSiglaEstado(dto.getNome(), SiglaEstado.valueOf(dto.getSiglaEstado())))
                .thenReturn(Optional.empty());

        when(partidaRepository.findAllByClube(clube))
                .thenReturn(List.of(partida));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clubeService.atualizarClube(1L, dto));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());

    }

    @Test
    void testUpdateClubWhenDataIsInvalid() {

        Clube clube = Clube.builder()
                .id(1L)
                .nome("Grêmio")
                .siglaEstado(SiglaEstado.valueOf("RS"))
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        ClubeRequestDTO dto = ClubeRequestDTO.builder()
                .nome("Grêmio FBPA")
                .siglaEstado("POA")
                .dataCriacao(LocalDate.of(2022, 9, 15))
                .ativo(false)
                .build();

        when(clubeRepository.findById(1L))
                .thenReturn(Optional.of(clube));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clubeService.atualizarClube(1L, dto));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testDisableClubWhenDataIsValid() {

        Clube clube = Clube.builder()
                .id(1L)
                .nome("Grêmio")
                .siglaEstado(SiglaEstado.valueOf("RS"))
                .dataCriacao(LocalDate.of(1903, 9, 15))
                .ativo(true)
                .build();

        when(clubeRepository.findById(1L))
                .thenReturn(Optional.of(clube));

        when(clubeRepository.save(clube))
                .thenReturn(clube);

        clubeService.inativarClube(1L);

        assertFalse(clube.getAtivo());
    }

    @Test
    void testDisableClubWhenDataIsInvalid() {

        when(clubeRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clubeService.inativarClube(1L));

        log.info("Exception: {}", ex.getMessage());

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }


}
