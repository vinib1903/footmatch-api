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
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final PartidaMapper partidaMapper;
    private final EstadioRepository estadioRepository;
    private final ClubeRepository clubeRepository;

    public PartidaResponseDTO criarPartida(PartidaRequestDTO dto) {

        Clube mandante = validarExistencia(clubeRepository.findById(dto.getMandanteId()), "Clube");
        Clube visitante = validarExistencia(clubeRepository.findById(dto.getVisitanteId()), "Clube");

        validarClubesAtivos(mandante, visitante);
        validarClubesDiferentes(dto.getMandanteId(), dto.getVisitanteId());
        validarDataPartidaAnteriorCriacaoClube(mandante, visitante, dto.getDataHora().toLocalDate());
        validarDataCriacaoFutura(dto.getDataHora());
        validarPartidasProximas(mandante, visitante, dto.getDataHora(), null);
        validarGolsNegativos(dto.getGolsMandante(), dto.getGolsVisitante());

        Estadio estadio = validarExistencia(estadioRepository.findById(dto.getEstadioId()), "Estádio");

        validarEstadioOcupado(estadio, dto.getDataHora().toLocalDate(), null);

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

    public Page<PartidaResponseDTO> obterPartidas(String clube, String estadio, Boolean goleada, String papel, Pageable pageable) {

        return partidaRepository.findPartidasWithFilters(clube, estadio, goleada, papel, pageable)
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

        Clube mandante = validarExistencia(clubeRepository.findById(dto.getMandanteId()), "Clube");

        Clube visitante = validarExistencia(clubeRepository.findById(dto.getVisitanteId()), "Clube");

        Estadio estadio = validarExistencia(estadioRepository.findById(dto.getEstadioId()), "Estádio");

        validarClubesAtivos(mandante, visitante);

        validarClubesDiferentes(dto.getMandanteId(), dto.getVisitanteId());

        validarDataPartidaAnteriorCriacaoClube(mandante, visitante, dto.getDataHora().toLocalDate());

        validarDataCriacaoFutura(dto.getDataHora());

        validarPartidasProximas(mandante, visitante, dto.getDataHora(), partida.getId());

        validarGolsNegativos(dto.getGolsMandante(), dto.getGolsVisitante());

        validarEstadioOcupado(estadio, dto.getDataHora().toLocalDate(), partida.getId());

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

    private void validarDataPartidaAnteriorCriacaoClube(Clube mandante, Clube visitante, LocalDate dataPartida) throws ResponseStatusException {

        if (dataPartida.isBefore(mandante.getDataCriacao()) || dataPartida.isBefore(visitante.getDataCriacao())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data anterior à criação de um dos clubes.");
        }
    }

    private void validarPartidasProximas(Clube mandante, Clube visitante, LocalDateTime dataHora, Long partidaId) throws ResponseStatusException {

        LocalDateTime inicio = dataHora.minusHours(48);
        LocalDateTime fim = dataHora.plusHours(48);

        boolean existePartidaMandante = partidaRepository.findAllByClube(mandante).stream()
                .anyMatch(p ->
                        (partidaId == null || !p.getId().equals(partidaId)) &&
                                !p.getDataHora().isBefore(inicio) && !p.getDataHora().isAfter(fim)
                );

        boolean existePartidaVisitante = partidaRepository.findAllByClube(visitante).stream()
                .anyMatch(p ->
                        (partidaId == null || !p.getId().equals(partidaId)) &&
                                !p.getDataHora().isBefore(inicio) && !p.getDataHora().isAfter(fim)
                );

        if (existePartidaMandante || existePartidaVisitante) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe partida para um dos clubes em menos de 48h.");
        }
    }

    private void validarDataCriacaoFutura(LocalDateTime dataHora) throws ResponseStatusException {

        if (dataHora.isAfter(LocalDateTime.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de criação não pode ser no futuro.");

    }

    private void validarGolsNegativos(Integer golsMandante, Integer golsVisitante) throws ResponseStatusException {

        if (golsMandante < 0 || golsVisitante < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gols não podem ser negativos.");
        }
    }

    private void validarClubesAtivos(Clube mandante, Clube visitante) throws ResponseStatusException {

        if (!mandante.getAtivo() || !visitante.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Clube inativo.");
        }
    }

    private <T> T validarExistencia(Optional<T> optional, String nome) throws ResponseStatusException {

        return optional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, nome + " não encontrado."));
    }

    private void validarClubesDiferentes(Long mandanteId, Long visitanteId) throws ResponseStatusException {

        if (mandanteId == visitanteId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clubes devem ser diferentes.");
        }
    }

    private void validarEstadioOcupado(Estadio estadio, LocalDate data, Long partidaId) throws ResponseStatusException {

        List<Partida> partidasNoDia = partidaRepository.findAllByEstadioAndData(estadio, data);

        boolean ocupado = partidasNoDia.stream()
                .anyMatch(p -> partidaId == null || !p.getId().equals(partidaId));
        if (ocupado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Estádio já possui partida neste dia.");
        }
    }
}