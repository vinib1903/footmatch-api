package com.teamcubation.footmatchapi.application.service.clube;

import com.teamcubation.footmatchapi.application.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.application.ports.out.ClubeEventsPort;
import com.teamcubation.footmatchapi.application.ports.out.ClubeRepository;
import com.teamcubation.footmatchapi.application.ports.out.PartidaRepository;
import com.teamcubation.footmatchapi.application.usecase.ClubeUseCases;
import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.domain.exceptions.EntidadeEmUsoException;
import com.teamcubation.footmatchapi.domain.exceptions.EntidadeNaoEncontradaException;
import com.teamcubation.footmatchapi.utils.mapper.ClubeMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@Service
public class ClubeServiceImpl implements ClubeUseCases {

    private final ClubeEventsPort clubeEventsPort;
    private final ClubeMapper clubeMapper;
    private final PartidaRepository partidaRepository;
    private final ClubeRepository clubeRepository;

    public ClubeServiceImpl(ClubeEventsPort clubeEventsPort, ClubeMapper clubeMapper, PartidaRepository partidaRepository, ClubeRepository clubeRepository) {
        this.clubeEventsPort = clubeEventsPort;
        this.clubeMapper = clubeMapper;
        this.partidaRepository = partidaRepository;
        this.clubeRepository = clubeRepository;
    }

    public ClubeResponseDTO criarClube(ClubeRequestDTO dto) {

        validarNomeClubeExistenteNoEstado(dto.getNome(), SiglaEstado.valueOf(dto.getSiglaEstado().toUpperCase()), null);

        Clube clube = Clube.criar(
                dto.getNome(),
                dto.getSiglaEstado(),
                dto.getAtivo(),
                dto.getDataCriacao()
        );

        clubeRepository.save(clube);

        return clubeMapper.entityToDto(clube);
    }

    public Page<ClubeResponseDTO> obterClubes(String nome, String siglaEstado, Boolean ativo, Pageable pageable) {

        SiglaEstado estado = null;

        if (siglaEstado != null) {
            estado = SiglaEstado.valueOf(siglaEstado);
        }

        return clubeRepository.findClubesWithFilters(nome, estado, ativo, pageable)
                .map(clubeMapper::entityToDto);
    }

    public ClubeResponseDTO obterClubePorId(Long id) {

        Clube clube = validarExistenciaClube(id);

        return clubeMapper.entityToDto(clube);
    }

    public ClubeResponseDTO atualizarClube(Long id, ClubeRequestDTO dto) {

        Clube clube = validarExistenciaClube(id);

        validarNomeClubeExistenteNoEstado(dto.getNome(), SiglaEstado.valueOf(dto.getSiglaEstado().toUpperCase()), id);

        if (!dto.getDataCriacao().isEqual(clube.getDataCriacao())) {
            Optional<Partida> partidaMaisAntiga = partidaRepository.findAllByClube(clube).stream()
                    .min(Comparator.comparing(Partida::getDataHora));
            if (partidaMaisAntiga.isPresent() && dto.getDataCriacao().isAfter(partidaMaisAntiga.get().getDataHora().toLocalDate())) {
                throw new EntidadeEmUsoException("Não é possível definir data de criação posterior a partidas já registradas para o clube.");
            }
        }

        Clube.criar(dto.getNome(), dto.getSiglaEstado(), dto.getAtivo(), dto.getDataCriacao());

        clube.setNome(dto.getNome());
        clube.setSiglaEstado(SiglaEstado.valueOf(dto.getSiglaEstado()));
        clube.setDataCriacao(dto.getDataCriacao());
        clube.setAtivo(dto.getAtivo());

        Clube salvo = clubeRepository.save(clube);

        return clubeMapper.entityToDto(salvo);
    }

    public void inativarClube(Long id) {

        Clube clube = validarExistenciaClube(id);

        clube.setAtivo(false);

        clubeRepository.save(clube);
    }

    @Override
    public void solicitarCriacaoClube(ClubeRequestDTO dto) {
        clubeEventsPort.notificarCriacaoClube(dto);
    }

    @Override
    public void solicitarAtualizacaoClube(Long id, ClubeRequestDTO dto) {
        clubeEventsPort.notificarAtualizacaoClube(id, dto);
    }

    @Override
    public void solicitarInativacaoClube(Long id) {
        clubeEventsPort.notificarInativacaoClube(id);
    }

    public Clube validarExistenciaClube(Long id) {

        return clubeRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Clube não encontrado."));
    }

    private void validarNomeClubeExistenteNoEstado(String nome, SiglaEstado siglaEstado, Long id) {

        Clube clubeExistente = clubeRepository.findByNomeAndSiglaEstado(nome, siglaEstado);

        if (clubeExistente != null && (id == null || !clubeExistente.getId().equals(id))) {
            throw new EntidadeEmUsoException("Já existe um clube com este nome no estado informado.");
        }
    }
}
