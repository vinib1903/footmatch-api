package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.mapper.EstadioMapper;
import com.teamcubation.footmatchapi.repository.EstadioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EstadioService {

    public final EstadioRepository estadioRepository;
    private final EstadioMapper estadioMapper;

    public EstadioResponseDTO criarEstadio(EstadioRequestDTO estadioRequestDTO) {

        if (estadioRequestDTO.getNome() == null || estadioRequestDTO.getNome().trim().length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do estádio deve ter pelo menos 3 letras.");
        }

        Optional<Estadio> estadioExistente = estadioRepository.findByNome (estadioRequestDTO.getNome());
        if (estadioExistente.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um estádio com esse nome.");
        }

        Estadio estadio = estadioMapper.toEntity(estadioRequestDTO);
        Estadio salvo = estadioRepository.save(estadio);
        return estadioMapper.toDto(salvo);
    }

    public List<EstadioResponseDTO> obterEstadios() {
        return null;
    }

    public EstadioResponseDTO obterEstadioPorId(Long id) {
        Estadio estadio = estadioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado."));
        return estadioMapper.toDto(estadio);
    }

    public EstadioResponseDTO atualizarEstadio(Long id, EstadioRequestDTO estadioRequestDTO) {

        Estadio estadio = estadioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado."));

        if (estadioRequestDTO.getNome() == null || estadioRequestDTO.getNome().trim().length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do estádio deve ter pelo menos 3 letras.");
        }

        Optional<Estadio> estadioExistente = estadioRepository.findByNome(estadioRequestDTO.getNome());
        if (estadioExistente.isPresent() && !estadioExistente.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um estádio com esse nome.");
        }

        estadio.setNome(estadioRequestDTO.getNome());
        Estadio salvo = estadioRepository.save(estadio);
        return estadioMapper.toDto(salvo);
    }

    public void deletarEstadio(Long id) {
    }
}
