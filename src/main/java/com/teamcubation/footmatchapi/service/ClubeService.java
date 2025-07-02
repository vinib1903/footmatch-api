package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.mapper.ClubeMapper;
import com.teamcubation.footmatchapi.repository.ClubeRepository;
import com.teamcubation.footmatchapi.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

        if (clubeRequestDTO.getNome() == null || clubeRequestDTO.getNome().trim().length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do clube deve ter pelo menos 2 letras.");
        }

        if (clubeRequestDTO.getSiglaEstado() == null || !SiglaEstado.isValido(clubeRequestDTO.getSiglaEstado())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sigla de estado inválida.");
        }

        if (clubeRequestDTO.getDataCriacao() == null || clubeRequestDTO.getDataCriacao().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de criação não pode ser no futuro.");
        }

        Optional<Clube> clubeExistente = clubeRepository.findByNomeAndSiglaEstado(clubeRequestDTO.getNome(), SiglaEstado.valueOf(clubeRequestDTO.getSiglaEstado()));
        if (clubeExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um clube com esse nome nesse estado.");
        }

        Clube clube = clubeMapper.toEntity(clubeRequestDTO);
        Clube salvo = clubeRepository.save(clube);
        return clubeMapper.toDto(salvo);
    }

    public Page<ClubeResponseDTO> obterClubes(String nome, String siglaEstado, Boolean ativo, Pageable pageable) {
        Specification<Clube> spec = Specification.where(null);

        if (nome != null && !nome.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
        }
        if (siglaEstado != null && !siglaEstado.isBlank() && SiglaEstado.isValido(siglaEstado)) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("siglaEstado"), SiglaEstado.valueOf(siglaEstado)));
        }
        if (ativo != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("ativo"), ativo));
        }

        return clubeRepository.findAll(spec, pageable).map(clubeMapper::toDto);
    }

    public ClubeResponseDTO obterClubePorId(Long id) {

        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));
        return clubeMapper.toDto(clube);
    }

    // TODO - Implementar validações adicionais
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
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));
        clube.setAtivo(false);
        clubeRepository.save(clube);
    }
}



