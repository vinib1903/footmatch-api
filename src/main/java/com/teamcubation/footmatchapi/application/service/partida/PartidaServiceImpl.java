package com.teamcubation.footmatchapi.application.service.partida;

import com.teamcubation.footmatchapi.application.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.PartidaResponseDTO;
import com.teamcubation.footmatchapi.application.ports.out.PartidaEventsPort;
import com.teamcubation.footmatchapi.application.ports.out.PartidaRepository;
import com.teamcubation.footmatchapi.application.usecase.ClubeUseCases;
import com.teamcubation.footmatchapi.application.usecase.EstadioUseCases;
import com.teamcubation.footmatchapi.application.usecase.PartidaUseCases;
import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.domain.exceptions.EntidadeEmUsoException;
import com.teamcubation.footmatchapi.domain.exceptions.EntidadeNaoEncontradaException;
import com.teamcubation.footmatchapi.utils.mapper.PartidaMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PartidaServiceImpl implements PartidaUseCases {

    private final PartidaRepository partidaRepository;
    private final PartidaMapper partidaMapper;
    private final EstadioUseCases estadioUseCases;
    private final ClubeUseCases clubeUseCases;
    private final PartidaEventsPort partidaEventsPort;

    public PartidaServiceImpl(PartidaRepository partidaRepository, PartidaMapper partidaMapper, EstadioUseCases estadioUseCases, ClubeUseCases clubeUseCases, PartidaEventsPort partidaEventsPort) {
        this.partidaRepository = partidaRepository;
        this.partidaMapper = partidaMapper;
        this.estadioUseCases = estadioUseCases;
        this.clubeUseCases = clubeUseCases;
        this.partidaEventsPort = partidaEventsPort;
    }

    public PartidaResponseDTO criarPartida(PartidaRequestDTO dto) {

        Clube mandante = clubeUseCases.validarExistenciaClube(dto.getMandanteId());
        Clube visitante = clubeUseCases.validarExistenciaClube(dto.getVisitanteId());
        Estadio estadio = estadioUseCases.validarExistenciaEstadio(dto.getEstadioId());

        // Validações que dependem de repositório permanecem no serviço
        validarPartidasProximas(mandante, visitante, dto.getDataHora(), null);
        validarEstadioOcupado(estadio, dto.getDataHora().toLocalDate(), null);

        // A criação e validação pura agora é responsabilidade da entidade
        Partida partida = Partida.criar(
                mandante,
                visitante,
                estadio,
                dto.getDataHora(),
                dto.getGolsMandante(),
                dto.getGolsVisitante()
        );

        partidaRepository.save(partida);

        return partidaMapper.EntityToDto(partida);
    }

    public Page<PartidaResponseDTO> obterPartidas(Long clubeId, Long estadioId, Boolean goleada, String papel, Pageable pageable) {

        Clube clube = (clubeId != null) ? clubeUseCases.validarExistenciaClube(clubeId) : null;
        Estadio estadio = (estadioId != null) ? estadioUseCases.validarExistenciaEstadio(estadioId) : null;

        return partidaRepository.findPartidasWithFilters(clube, estadio, goleada, papel, pageable)
                .map(partidaMapper::EntityToDto);
    }

    public PartidaResponseDTO obterPartidaPorId(Long id) {

        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Partida com id " + id + " não encontrada."));
        return partidaMapper.EntityToDto(partida);
    }

    public PartidaResponseDTO atualizarPartida(Long id, PartidaRequestDTO dto) {

        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Partida com id " + id + " não encontrada."));

        Clube mandante = clubeUseCases.validarExistenciaClube(dto.getMandanteId());
        Clube visitante = clubeUseCases.validarExistenciaClube(dto.getVisitanteId());
        Estadio estadio = estadioUseCases.validarExistenciaEstadio(dto.getEstadioId());

        // Validações que dependem de repositório permanecem no serviço
        validarPartidasProximas(mandante, visitante, dto.getDataHora(), partida.getId());
        validarEstadioOcupado(estadio, dto.getDataHora().toLocalDate(), partida.getId());

        // A entidade não é recriada, apenas atualizada.
        partida.setMandante(mandante);
        partida.setVisitante(visitante);
        partida.setEstadio(estadio);
        partida.setDataHora(dto.getDataHora());
        partida.setGolsMandante(dto.getGolsMandante());
        partida.setGolsVisitante(dto.getGolsVisitante());

        Partida salva = partidaRepository.save(partida);

        return partidaMapper.EntityToDto(salva);
    }

    public void deletarPartida(Long id) {

        partidaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Partida com id " + id + " não encontrada."));

        partidaRepository.deletePartida(id);
    }

    @Override
    public void solicitarCriacaoPartida(PartidaRequestDTO dto) {
        partidaEventsPort.notificarCriacaoPartida(dto);
    }

    @Override
    public void solicitarAtualizacaoPartida(Long id, PartidaRequestDTO dto) {
        partidaEventsPort.notificarAtualizacaoPartida(id, dto);
    }

    @Override
    public void solicitarExclusaoPartida(Long id) {
        partidaEventsPort.notificarExclusaoPartida(id);
    }

    private void validarPartidasProximas(Clube mandante, Clube visitante, LocalDateTime dataHora, Long partidaId) {

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
            throw new EntidadeEmUsoException("Já existe partida para um dos clubes em menos de 48h.");
        }
    }

    private void validarEstadioOcupado(Estadio estadio, LocalDate data, Long partidaId) {

        List<Partida> partidasNoDia = partidaRepository.findAllByEstadioAndData(estadio, data);

        boolean ocupado = partidasNoDia.stream()
                .anyMatch(p -> partidaId == null || !p.getId().equals(partidaId));

        if (ocupado) {
            throw new EntidadeEmUsoException("Estádio já possui partida neste dia.");
        }
    }
}