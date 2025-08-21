package com.teamcubation.footmatchapi.application.service.retrospecto;

import com.teamcubation.footmatchapi.application.dto.response.*;
import com.teamcubation.footmatchapi.application.usecase.RetrospectoUseCases;
import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.domain.interfaces.ClubeRepository;
import com.teamcubation.footmatchapi.domain.interfaces.PartidaRepository;
import com.teamcubation.footmatchapi.utils.mapper.PartidaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetrospectoServiceImpl implements RetrospectoUseCases {

    private final ClubeRepository clubeRepository;
    private final PartidaRepository partidaRepository;
    private final PartidaMapper partidaMapper;

    public ClubeRetrospectoResponseDTO obterRetrospecto(Long id, String papel) {

        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));

        List<Partida> partidas = filtrarPorPapel(partidaRepository.findAllByClube(clube), clube, papel);

        int vitorias = 0, empates = 0, derrotas = 0, golsMarcados = 0, golsSofridos = 0;

        for (Partida p : partidas) {
            boolean clubeMandante = p.getMandante().getId().equals(clube.getId());
            int golsPro = clubeMandante ? p.getGolsMandante() : p.getGolsVisitante();
            int golsContra = clubeMandante ? p.getGolsVisitante() : p.getGolsMandante();

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

        return ClubeRetrospectoResponseDTO.builder()
                .partidas(partidas.size())
                .vitorias(vitorias)
                .empates(empates)
                .derrotas(derrotas)
                .golsMarcados(golsMarcados)
                .golsSofridos(golsSofridos)
                .build();
    }

    public Page<ClubeRestrospectoAdversarioResponseDTO> obterRestrospectoAdversarios(Long id, String papel, Pageable pageable) {
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));

        List<Partida> partidas = filtrarPorPapel(partidaRepository.findAllByClube(clube), clube, papel);

        Map<Long, ClubeRestrospectoAdversarioResponseDTO> adversariosMap = new HashMap<>();

        for (Partida p : partidas) {
            boolean clubeMandante = p.getMandante().getId().equals(clube.getId());
            Clube adversario = clubeMandante ? p.getVisitante() : p.getMandante();
            Long adversarioId = adversario.getId();

            adversariosMap.computeIfAbsent(adversarioId, idKey -> ClubeRestrospectoAdversarioResponseDTO.builder()
                    .adversarioNome(adversario.getNome() + "-" + adversario.getSiglaEstado())
                    .partidas(0)
                    .vitorias(0)
                    .empates(0)
                    .derrotas(0)
                    .golsMarcados(0)
                    .golsSofridos(0)
                    .build());

            ClubeRestrospectoAdversarioResponseDTO dto = adversariosMap.get(adversarioId);

            int golsPro = clubeMandante ? p.getGolsMandante() : p.getGolsVisitante();
            int golsContra = clubeMandante ? p.getGolsVisitante() : p.getGolsMandante();

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

        List<ClubeRestrospectoAdversarioResponseDTO> lista = new ArrayList<>(adversariosMap.values());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), lista.size());
        List<ClubeRestrospectoAdversarioResponseDTO> pageContent = (start >= lista.size()) ? List.of() : lista.subList(start, end);

        return new PageImpl<>(pageContent, pageable, lista.size());
    }

    public ConfrontoDiretoResponseDTO obterConfrontoDireto(Long clubeId, Long adversarioId, String papel) {

        Clube clube = clubeRepository.findById(clubeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));
        Clube adversario = clubeRepository.findById(adversarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Adversário não encontrado"));

        List<Partida> partidas = filtrarPorPapel(partidaRepository.findAllByClubes(clube, adversario), clube, papel);

        int vitoriasClube = 0, empates = 0, golsProClube = 0, vitoriasAdversario = 0, golsProAdversario = 0;

        for (Partida p : partidas) {

            boolean clubeMandante = p.getMandante().getId().equals(clube.getId());
            int golsClube = clubeMandante ? p.getGolsMandante() : p.getGolsVisitante();
            int golsAdversario = clubeMandante ? p.getGolsVisitante() : p.getGolsMandante();

            golsProClube += golsClube;
            golsProAdversario += golsAdversario;

            if (golsClube > golsAdversario) {
                vitoriasClube++;
            } else if (golsAdversario > golsClube) {
                vitoriasAdversario++;
            } else {
                empates++;
            }
        }

        ClubeRestrospectoAdversarioResponseDTO retrospectoClube = criarRetrospecto(
                adversario.getNome() + " - " + adversario.getSiglaEstado(),
                partidas.size(),
                vitoriasClube,
                empates,
                vitoriasAdversario,
                golsProClube,
                golsProAdversario
        );

        ClubeRestrospectoAdversarioResponseDTO retrospectoAdversario = criarRetrospecto(
                clube.getNome() + " - " + clube.getSiglaEstado(),
                partidas.size(),
                vitoriasAdversario,
                empates,
                vitoriasClube,
                golsProAdversario,
                golsProClube
        );

        List<PartidaResponseDTO> partidaDTOs = partidas.stream()
                .map(partidaMapper::toDto)
                .collect(Collectors.toList());

        return ConfrontoDiretoResponseDTO.builder()
                .clube(retrospectoClube)
                .adversario(retrospectoAdversario)
                .partidas(partidaDTOs)
                .build();
    }

    public Page<RankingResponseDTO> obterRanking(String criterio, Pageable pageable) {

        List<Clube> clubes = clubeRepository.findAll();
        List<RankingResponseDTO> ranking = new ArrayList<>();

        for (Clube clube : clubes) {

            List<Partida> partidasJogadas = partidaRepository.findAllByClube(clube);
            int vitorias = 0, empates = 0, golsMarcados = 0, partidas = partidasJogadas.size(), pontos = 0;

            for (Partida p : partidasJogadas) {
                boolean clubeMandante = p.getMandante().getId().equals(clube.getId());
                int golsPro = clubeMandante ? p.getGolsMandante() : p.getGolsVisitante();
                int golsContra = clubeMandante ? p.getGolsVisitante() : p.getGolsMandante();

                golsMarcados += golsPro;
                if (golsPro > golsContra) {
                    vitorias++;
                } else if (golsPro == golsContra) {
                    empates++;
                }


            }
            pontos = (vitorias * 3) + empates;
            ranking.add(RankingResponseDTO.builder()
                    .nome(clube.getNome() + "-" + clube.getSiglaEstado())
                    .partidas(partidas)
                    .vitorias(vitorias)
                    .empates(empates)
                    .golsMarcados(golsMarcados)
                    .pontos(pontos)
                    .build());
        }

        switch (criterio) {
            case "vitorias":
                ranking = ranking.stream().filter(r -> r.getVitorias() > 0)
                        .sorted((a, b) -> Integer.compare(b.getVitorias(), a.getVitorias()))
                        .collect(Collectors.toList());
                break;
            case "gols":
                ranking = ranking.stream().filter(r -> r.getGolsMarcados() > 0)
                        .sorted((a, b) -> Integer.compare(b.getGolsMarcados(), a.getGolsMarcados()))
                        .collect(Collectors.toList());
                break;
            case "partidas":
                ranking = ranking.stream().filter(r -> r.getPartidas() > 0)
                        .sorted((a, b) -> Integer.compare(b.getPartidas(), a.getPartidas()))
                        .collect(Collectors.toList());
                break;
            case "pontos":
            default:
                ranking = ranking.stream().filter(r -> r.getPontos() > 0)
                        .sorted((a, b) -> Integer.compare(b.getPontos(), a.getPontos()))
                        .collect(Collectors.toList());
                break;
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), ranking.size());

        List<RankingResponseDTO> pageContent = (start >= ranking.size()) ? List.of() : ranking.subList(start, end);

        return new PageImpl<>(pageContent, pageable, ranking.size());
    }

    private List<Partida> filtrarPorPapel(List<Partida> partidas, Clube clube, String papel) {
        if ("mandante".equalsIgnoreCase(papel)) {
            return partidas.stream()
                    .filter(p -> p.getMandante().getId().equals(clube.getId()))
                    .collect(Collectors.toList());
        } else if ("visitante".equalsIgnoreCase(papel)) {
            return partidas.stream()
                    .filter(p -> p.getVisitante().getId().equals(clube.getId()))
                    .collect(Collectors.toList());
        }
        return partidas;
    }

    private ClubeRestrospectoAdversarioResponseDTO criarRetrospecto(
            String adversarioNome,
            int partidas,
            int vitorias,
            int empates,
            int derrotas,
            int golsMarcados,
            int golsSofridos) {

        return ClubeRestrospectoAdversarioResponseDTO.builder()
                .adversarioNome(adversarioNome)
                .partidas(partidas)
                .vitorias(vitorias)
                .empates(empates)
                .derrotas(derrotas)
                .golsMarcados(golsMarcados)
                .golsSofridos(golsSofridos)
                .build();
    }
}
