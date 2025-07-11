package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.mapper.EstadioMapper;
import com.teamcubation.footmatchapi.repository.EstadioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EstadioService {

    public final EstadioRepository estadioRepository;
    private final EstadioMapper estadioMapper;

    public EstadioResponseDTO criarEstadio(EstadioRequestDTO estadioRequestDTO) {

        validarNomeExistente(estadioRequestDTO.getNome(), null);

        Estadio estadio = estadioMapper.toEntity(estadioRequestDTO);
        estadioRepository.save(estadio);

        return estadioMapper.toDto(estadio);
    }

    public Page<EstadioResponseDTO> obterEstadios(Pageable pageable) {

        return estadioRepository.findAll(pageable).map(estadioMapper::toDto);
    }

    public EstadioResponseDTO obterEstadioPorId(Long id) {

        Estadio estadio = estadioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado."));
        return estadioMapper.toDto(estadio);
    }

    public EstadioResponseDTO atualizarEstadio(Long id, EstadioRequestDTO estadioRequestDTO) {

        Estadio estadio = estadioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado."));

        validarNomeExistente(estadioRequestDTO.getNome(), id);

        estadio.setNome(estadioRequestDTO.getNome());

        Estadio salvo = estadioRepository.save(estadio);
        return estadioMapper.toDto(salvo);
    }

    private void validarNomeExistente(String nome, Long id) throws ResponseStatusException {

        Optional<Estadio> estadioExistente = estadioRepository.findByNome(nome);

        if (estadioExistente.isPresent() && !estadioExistente.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um estádio com este nome.");
        }
    }

}
