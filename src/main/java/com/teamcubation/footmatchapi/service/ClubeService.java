package com.teamcubation.footmatchapi.service;

import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.dto.response.ClubeResponseDTO;
import com.teamcubation.footmatchapi.dto.response.ClubeRestrospectoAdversariosResponseDTO;
import com.teamcubation.footmatchapi.dto.response.ClubeRetrospectoResponseDTO;
import com.teamcubation.footmatchapi.mapper.ClubeMapper;
import com.teamcubation.footmatchapi.repository.ClubeRepository;
import com.teamcubation.footmatchapi.repository.PartidaRepository;
import jakarta.persistence.criteria.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ClubeService {

    private final ClubeRepository clubeRepository;
    private final PartidaRepository partidaRepository;
    private final ClubeMapper clubeMapper;

    //TODO: Verificar retorno (concatenar na mesma linha nome e estadio assim como fiz no obterRestrospectoAdversarios, padronizar outros metodos)
    public ClubeResponseDTO criarClube(ClubeRequestDTO dto) {

        if (!Arrays.stream(SiglaEstado.values()).anyMatch(estado -> estado.name().equals(dto.getSiglaEstado()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado inválido.");
        }

        if (dto.getDataCriacao().isAfter(java.time.LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de criação não pode ser no futuro.");
        }

        Optional<Clube> clubeExistente = clubeRepository.findByNomeAndSiglaEstado(dto.getNome(), SiglaEstado.valueOf(dto.getSiglaEstado()));
        if (clubeExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um clube com este nome no estado informado.");
        }

        Clube clube = clubeMapper.toEntity(dto);
        clubeRepository.save(clube);

        return clubeMapper.toDto(clube);
    }

    public Page<ClubeResponseDTO> obterClubes(String nome, String siglaEstado, Boolean ativo, Pageable pageable) {
        SiglaEstado estado = null;
        if (siglaEstado != null) {
            try {
                estado = SiglaEstado.valueOf(siglaEstado);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado inválido.");
            }
        }

        return clubeRepository.findClubesWichFilters(nome, estado, ativo, pageable)
                .map(clubeMapper::toDto);
    }

    public ClubeResponseDTO obterClubePorId(Long id) {
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));
        return clubeMapper.toDto(clube);
    }

    public ClubeResponseDTO atualizarClube(Long id, ClubeRequestDTO dto) {

        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));

        if (!Arrays.stream(SiglaEstado.values()).anyMatch(estado -> estado.name().equals(dto.getSiglaEstado()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado inválido.");
        }

        if (dto.getDataCriacao().isAfter(java.time.LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de criação não pode ser no futuro.");
        }

        Optional<Clube> clubeExistente = clubeRepository.findByNomeAndSiglaEstado(dto.getNome(), SiglaEstado.valueOf(dto.getSiglaEstado()));
        if (clubeExistente.isPresent() && !clubeExistente.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um clube com este nome no estado informado.");
        }

        if (!dto.getDataCriacao().isEqual(clube.getDataCriacao())) {
            var dataLimite = dto.getDataCriacao().plusDays(1).atTime(LocalTime.MIN);
            boolean existePartidaDepois = partidaRepository.existsPartidaAfterDate(clube, dataLimite);
            if (existePartidaDepois) {
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
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));
        clube.setAtivo(false);
        clubeRepository.save(clube);
    }

    public ClubeRetrospectoResponseDTO obterRetrospecto(Long id) {

        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));

        List<Partida> partidas = partidaRepository.FindAllByClube(clube);

        int vitorias = 0, empates = 0, derrotas = 0, golsMarcados = 0, golsSofridos = 0;

        for (Partida p : partidas) {
            boolean mandante = p.getMandante().getId().equals(clube.getId());
            int golsPro = mandante ? p.getGolsMandante() : p.getGolsVisitante();
            int golsContra = mandante ? p.getGolsVisitante() : p.getGolsMandante();

            golsMarcados += golsPro;
            golsSofridos += golsContra;

            if (golsPro > golsContra) {
                vitorias++;
            } else if (golsPro < golsContra) {
                derrotas++;
            } else {
                empates++;
            }
        }

        int totalPartidas = partidas.size();

        return ClubeRetrospectoResponseDTO.builder()
                .partidas(totalPartidas)
                .vitorias(vitorias)
                .empates(empates)
                .derrotas(derrotas)
                .golsMarcados(golsMarcados)
                .golsSofridos(golsSofridos)
                .build();
    }

    public Page<ClubeRestrospectoAdversariosResponseDTO> obterRestrospectoAdversarios(Long id, Pageable pageable) {
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));

        List<Partida> partidas = partidaRepository.FindAllByClube(clube);

        Map<Long, ClubeRestrospectoAdversariosResponseDTO> map = new HashMap<>();

        for (Partida p : partidas) {
            boolean mandante = p.getMandante().getId().equals(clube.getId());
            Clube adversario = mandante ? p.getVisitante() : p.getMandante();
            Long adversarioId = adversario.getId();

            map.computeIfAbsent(adversarioId, idKey -> ClubeRestrospectoAdversariosResponseDTO.builder()
                    //.adversarioId(adversarioId)
                    .adversarioNome(adversario.getNome() + "-" + adversario.getSiglaEstado())
                    //.adversarioSiglaEstado(adversario.getSiglaEstado())
                    .partidas(0)
                    .vitorias(0)
                    .empates(0)
                    .derrotas(0)
                    .golsMarcados(0)
                    .golsSofridos(0)
                    .build());

            ClubeRestrospectoAdversariosResponseDTO dto = map.get(adversarioId);

            int golsPro = mandante ? p.getGolsMandante() : p.getGolsVisitante();
            int golsContra = mandante ? p.getGolsVisitante() : p.getGolsMandante();

            dto.setPartidas(dto.getPartidas() + 1);
            dto.setGolsMarcados(dto.getGolsMarcados() + golsPro);
            dto.setGolsSofridos(dto.getGolsSofridos() + golsContra);

            if (golsPro > golsContra) {
                dto.setVitorias(dto.getVitorias() + 1);
            } else if (golsPro < golsContra) {
                dto.setDerrotas(dto.getDerrotas() + 1);
            } else {
                dto.setEmpates(dto.getEmpates() + 1);
            }
        }

        List<ClubeRestrospectoAdversariosResponseDTO> lista = new ArrayList<>(map.values());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), lista.size());
        List<ClubeRestrospectoAdversariosResponseDTO> pageContent = (start > end) ? List.of() : lista.subList(start, end);

        return new PageImpl<>(pageContent, pageable, lista.size());
    }
}




