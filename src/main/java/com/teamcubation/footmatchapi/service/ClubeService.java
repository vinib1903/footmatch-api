package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.mapper.ClubeMapper;
import com.teamcubation.footmatchapi.repository.ClubeRepository;
import com.teamcubation.footmatchapi.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubeService {

    private final ClubeRepository clubeRepository;
    private final ClubeMapper clubeMapper;
    private final PartidaRepository partidaRepository;

    public ClubeResponseDTO criarClube(ClubeRequestDTO clubeRequestDTO) {
        Clube clube = clubeMapper.toEntity(clubeRequestDTO);
        Clube salvo = clubeRepository.save(clube);
        return clubeMapper.toDto(salvo);
    }

    public List<ClubeResponseDTO> obterClubes() {

        return clubeRepository.findAll()
                .stream()
                .map(clubeMapper::toDto)
                .toList();
    }

    public ClubeResponseDTO obterClubePorId(Long id) {

        return clubeRepository.findById(id)
                .map(clubeMapper::toDto)
                .orElse(null);
    }

    public ClubeResponseDTO atualizarClube(Long id, ClubeRequestDTO dto) {
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));

        if (dto.getNome() == null || dto.getNome().trim().length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do clube deve ter pelo menos 2 letras.");
        }

        try {
            SiglaEstado.valueOf(dto.getSiglaEstado());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sigla de estado inválida.");
        }

        if (dto.getDataCriacao() == null || dto.getDataCriacao().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de criação não pode ser no futuro.");
        }

        // Validação: nome duplicado no mesmo estado (desconsiderando o próprio clube)
        /*Optional<Clube> existente = clubeRepository.findByNomeAndSiglaEstado(dto.getNome(), SiglaEstado.valueOf(dto.getSiglaEstado()));
        if (existente.isPresent() && !existente.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um clube com esse nome nesse estado.");
        }*/

        // Validação: data de criação não pode ser posterior a alguma partida já registrada
        /*boolean existePartidaDepois = partidaRepository.existsByClubeAndDataAfter(clube, dto.getDataCriacao());
        if (existePartidaDepois) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é possível definir data de criação posterior a partidas já registradas para o clube.");
        }*/

        clube.setNome(dto.getNome());
        clube.setSiglaEstado(SiglaEstado.valueOf(dto.getSiglaEstado()));
        clube.setDataCriacao(dto.getDataCriacao());
        clube.setAtivo(dto.getAtivo());

        Clube salvo = clubeRepository.save(clube);
        return clubeMapper.toDto(salvo);
    }

    public void inativarClube(Long id) {
        clubeRepository.findById(id).ifPresent(clube -> {
            clube.setAtivo(false);
            clubeRepository.save(clube);
        });
    }
}



