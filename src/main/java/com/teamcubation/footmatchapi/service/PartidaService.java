package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.dto.response.PartidaResponseDTO;
import com.teamcubation.footmatchapi.mapper.PartidaMapper;
import com.teamcubation.footmatchapi.repository.ClubeRepository;
import com.teamcubation.footmatchapi.repository.EstadioRepository;
import com.teamcubation.footmatchapi.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final PartidaMapper partidaMapper;
    private final EstadioRepository estadioRepository;
    private final ClubeRepository clubeRepository;

    public PartidaResponseDTO criarPartida(PartidaRequestDTO dto) {

        if (dto.getMandanteId().equals(dto.getVisitanteId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clubes devem ser diferentes.");
        }

        Clube mandante = clubeRepository.findById(dto.getMandanteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mandante não existe."));
        Clube visitante = clubeRepository.findById(dto.getVisitanteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Visitante não existe."));
        Estadio estadio = estadioRepository.findById(dto.getEstadioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estádio não existe."));

        if (dto.getGolsMandante() < 0 || dto.getGolsVisitante() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gols não podem ser negativos.");
        }

        if (dto.getDataHora().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data/hora não pode ser no futuro.");
        }

        if (dto.getDataHora().toLocalDate().isBefore(mandante.getDataCriacao()) ||
                dto.getDataHora().toLocalDate().isBefore(visitante.getDataCriacao())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data/hora anterior à criação de um dos clubes.");
        }

        if (!mandante.getAtivo() || !visitante.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Clube inativo.");
        }

        LocalDateTime inicio = dto.getDataHora().minusHours(48);
        LocalDateTime fim = dto.getDataHora().plusHours(48);
        boolean existePartidaProxima = partidaRepository.existsByClubeAndDataHoraBetween(mandante, inicio, fim)
                || partidaRepository.existsByClubeAndDataHoraBetween(visitante, inicio, fim);
        if (existePartidaProxima) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe partida para um dos clubes em menos de 48h.");
        }

        boolean estadioOcupado = partidaRepository.existsByEstadioAndData(estadio, dto.getDataHora().toLocalDate());
        if (estadioOcupado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Estádio já possui partida neste dia.");
        }

        Partida partida = Partida.builder()
                .mandante(mandante)
                .visitante(visitante)
                .estadio(estadio)
                .dataHora(dto.getDataHora())
                .golsMandante(dto.getGolsMandante())
                .golsVisitante(dto.getGolsVisitante())
                .build();
        partidaRepository.save(partida);

        return partidaMapper.toDto(partida);

    }

    public Page<PartidaResponseDTO> obterPartidas(Long clubeId, Long estadioId, Pageable pageable) {
        Clube clube = null;
        Estadio estadio = null;

        if (clubeId != null) {
            clube = clubeRepository.findById(clubeId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado."));
        }
        if (estadioId != null) {
            estadio = estadioRepository.findById(estadioId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado."));
        }

        return partidaRepository.findPartidasComFiltros(clube, estadio, pageable)
                .map(partidaMapper::toDto);
    }

    public PartidaResponseDTO obterPartidaPorId(Long id) {

        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada."));
        return partidaMapper.toDto(partida);
    }

    public PartidaResponseDTO atualizarPartida(Long id, PartidaRequestDTO dto) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada."));

        if (dto.getMandanteId() == null || dto.getVisitanteId() == null ||
                dto.getEstadioId() == null || dto.getDataHora() == null ||
                dto.getGolsMandante() == null || dto.getGolsVisitante() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados obrigatórios ausentes.");
        }

        if (dto.getMandanteId().equals(dto.getVisitanteId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clubes devem ser diferentes.");
        }

        Clube mandante = clubeRepository.findById(dto.getMandanteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mandante não existe."));
        Clube visitante = clubeRepository.findById(dto.getVisitanteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Visitante não existe."));
        Estadio estadio = estadioRepository.findById(dto.getEstadioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estádio não existe."));

        if (dto.getGolsMandante() < 0 || dto.getGolsVisitante() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gols não podem ser negativos.");
        }

        if (dto.getDataHora().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data/hora não pode ser no futuro.");
        }

        if (dto.getDataHora().toLocalDate().isBefore(mandante.getDataCriacao()) ||
                dto.getDataHora().toLocalDate().isBefore(visitante.getDataCriacao())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data/hora anterior à criação de um dos clubes.");
        }

        if (!mandante.getAtivo() || !visitante.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Clube inativo.");
        }

        LocalDateTime inicio = dto.getDataHora().minusHours(48);
        LocalDateTime fim = dto.getDataHora().plusHours(48);
        boolean existePartidaProximaMandante = partidaRepository.existsByClubeAndDataHoraBetween(mandante, inicio, fim)
                && !(partida.getMandante().getId().equals(mandante.getId()) && partida.getDataHora().equals(dto.getDataHora()));
        boolean existePartidaProximaVisitante = partidaRepository.existsByClubeAndDataHoraBetween(visitante, inicio, fim)
                && !(partida.getVisitante().getId().equals(visitante.getId()) && partida.getDataHora().equals(dto.getDataHora()));
        if (existePartidaProximaMandante || existePartidaProximaVisitante) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe partida para um dos clubes em menos de 48h.");
        }

        boolean estadioOcupado = partidaRepository.existsByEstadioAndData(estadio, dto.getDataHora().toLocalDate())
                && !(partida.getEstadio().getId().equals(estadio.getId()) && partida.getDataHora().toLocalDate().equals(dto.getDataHora().toLocalDate()));
        if (estadioOcupado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Estádio já possui partida neste dia.");
        }

        partida.setMandante(mandante);
        partida.setVisitante(visitante);
        partida.setEstadio(estadio);
        partida.setDataHora(dto.getDataHora());
        partida.setGolsMandante(dto.getGolsMandante());
        partida.setGolsVisitante(dto.getGolsVisitante());

        Partida salva = partidaRepository.save(partida);
        return partidaMapper.toDto(salva);
    }

    public void deletarPartida(Long id) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada."));

        partidaRepository.delete(partida);
    }
}
