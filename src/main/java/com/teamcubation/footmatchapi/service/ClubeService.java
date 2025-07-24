package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.dto.response.*;
import com.teamcubation.footmatchapi.mapper.ClubeMapper;
import com.teamcubation.footmatchapi.repository.ClubeRepository;
import com.teamcubation.footmatchapi.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ClubeService {

    private final ClubeRepository clubeRepository;
    private final ClubeMapper clubeMapper;
    private final PartidaRepository partidaRepository;

    public ClubeResponseDTO criarClube(ClubeRequestDTO dto) {

        validarSiglaEstado(dto.getSiglaEstado());

        validarDataCriacaoFutura(dto.getDataCriacao());

        validarNomeClubeExistenteNoEstado(dto.getNome(), SiglaEstado.valueOf(dto.getSiglaEstado()), null);

        Clube clube = clubeMapper.toEntity(dto);

        clubeRepository.save(clube);

        return clubeMapper.toDto(clube);
    }

    public Page<ClubeResponseDTO> obterClubes(String nome, String siglaEstado, Boolean ativo, Pageable pageable) {

        SiglaEstado estado = null;

        if (siglaEstado != null) {
            estado = SiglaEstado.valueOf(siglaEstado);
        }

        return clubeRepository.findClubesWichFilters(nome, estado, ativo, pageable)
                .map(clubeMapper::toDto);
    }

    public ClubeResponseDTO obterClubePorId(Long id) {

        Clube clube = validarExistenciaClube(id);

        return clubeMapper.toDto(clube);
    }

    public ClubeResponseDTO atualizarClube(Long id, ClubeRequestDTO dto) {

        Clube clube = validarExistenciaClube(id);

        validarSiglaEstado(dto.getSiglaEstado());

        validarDataCriacaoFutura(dto.getDataCriacao());

        validarNomeClubeExistenteNoEstado(dto.getNome(), SiglaEstado.valueOf(dto.getSiglaEstado()), id);

        //TODO: da pra melhorar?
        if (!dto.getDataCriacao().isEqual(clube.getDataCriacao())) {
            Optional<Partida> partidaMaisAntiga = partidaRepository.findAllByClube(clube).stream()
                    .min(Comparator.comparing(Partida::getDataHora));
            if (partidaMaisAntiga.isPresent() && dto.getDataCriacao().isAfter(partidaMaisAntiga.get().getDataHora().toLocalDate())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é possível definir data de criação posterior a partidas já registradas para o clube.");
            }
        }

        clube.setNome(dto.getNome());
        clube.setSiglaEstado(SiglaEstado.valueOf(dto.getSiglaEstado()));
        clube.setDataCriacao(dto.getDataCriacao());
        clube.setAtivo(dto.getAtivo());

        Clube salvo = clubeRepository.save(clube);

        return clubeMapper.toDto(salvo);
    }

    public void inativarClube(Long id) {

        Clube clube = validarExistenciaClube(id);

        clube.setAtivo(false);

        clubeRepository.save(clube);
    }

    public Clube validarExistenciaClube(Long id) {

        return clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado."));
    }

    private void validarSiglaEstado(String siglaEstado) throws ResponseStatusException {

        if (Arrays.stream(SiglaEstado.values()).noneMatch(e -> e.name().equalsIgnoreCase(siglaEstado))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado inválido.");
        }
    }

    private void validarDataCriacaoFutura(LocalDate dataCriacao) throws ResponseStatusException {

        if (dataCriacao.isAfter(LocalDate.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de criação não pode ser no futuro.");

    }

    private void validarNomeClubeExistenteNoEstado(String nome, SiglaEstado siglaEstado, Long id) throws ResponseStatusException {

        Optional<Clube> clubeExistente = clubeRepository.findByNomeAndSiglaEstado(nome, siglaEstado);

        if (clubeExistente.isPresent() && !clubeExistente.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um clube com este nome no estado informado.");
        }
    }
}




