package com.teamcubation.footmatchapi.service;
import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.dto.response.ClubeRestrospectoAdversarioResponseDTO;
import com.teamcubation.footmatchapi.dto.response.ClubeRetrospectoResponseDTO;
import com.teamcubation.footmatchapi.dto.response.ConfrontoDiretoResponseDTO;
import com.teamcubation.footmatchapi.dto.response.PartidaResponseDTO;
import com.teamcubation.footmatchapi.mapper.PartidaMapper;
import com.teamcubation.footmatchapi.repository.ClubeRepository;
import com.teamcubation.footmatchapi.repository.PartidaRepository;
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
public class RetrospectoService {

    private final ClubeRepository clubeRepository;
    private final PartidaRepository partidaRepository;
    private final PartidaMapper partidaMapper;

    public ClubeRetrospectoResponseDTO obterRetrospecto(Long id) {

        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));

        List<Partida> partidas = partidaRepository.findAllByClube(clube);

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

    public Page<ClubeRestrospectoAdversarioResponseDTO> obterRestrospectoAdversarios(Long id, Pageable pageable) {
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));

        List<Partida> partidas = partidaRepository.findAllByClube(clube);

        Map<Long, ClubeRestrospectoAdversarioResponseDTO> map = new HashMap<>();

        for (Partida p : partidas) {
            boolean mandante = p.getMandante().getId().equals(clube.getId());
            Clube adversario = mandante ? p.getVisitante() : p.getMandante();
            Long adversarioId = adversario.getId();

            map.computeIfAbsent(adversarioId, idKey -> ClubeRestrospectoAdversarioResponseDTO.builder()
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

            ClubeRestrospectoAdversarioResponseDTO dto = map.get(adversarioId);

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

        List<ClubeRestrospectoAdversarioResponseDTO> lista = new ArrayList<>(map.values());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), lista.size());
        List<ClubeRestrospectoAdversarioResponseDTO> pageContent = (start > end) ? List.of() : lista.subList(start, end);

        return new PageImpl<>(pageContent, pageable, lista.size());
    }

    //TODO: Verificar otimizacoes do metodo e padronizacao
    public ConfrontoDiretoResponseDTO obterConfrontoDireto(Long clubeId, Long adversarioId) {

        Clube clube = clubeRepository.findById(clubeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));
        Clube adversario = clubeRepository.findById(adversarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Adversário não encontrado"));

        if (clube.equals(adversario)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Os clubes devem ser diferentes");
        }

        List<Partida> partidas = partidaRepository.findAllByClubes(clube, adversario);

        int vitoriasClube = 0, empates = 0, golsProClube = 0;
        int vitoriasAdversario = 0, golsProAdversario = 0;

        for (Partida p : partidas) {
            boolean clubeEhMandante = p.getMandante().getId().equals(clube.getId());

            int golsClube = clubeEhMandante ? p.getGolsMandante() : p.getGolsVisitante();
            int golsAdversario = clubeEhMandante ? p.getGolsVisitante() : p.getGolsMandante();

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

        ClubeRestrospectoAdversarioResponseDTO retrospectoClube = ClubeRestrospectoAdversarioResponseDTO.builder()
                .adversarioNome(adversario.getNome() + " - " + adversario.getSiglaEstado())
                .partidas(partidas.size())
                .vitorias(vitoriasClube)
                .empates(empates)
                .derrotas(vitoriasAdversario)
                .golsMarcados(golsProClube)
                .golsSofridos(golsProAdversario)
                .build();

        ClubeRestrospectoAdversarioResponseDTO retrospectoAdversario = ClubeRestrospectoAdversarioResponseDTO.builder()
                .adversarioNome(clube.getNome() + " - " + clube.getSiglaEstado())
                .partidas(partidas.size())
                .vitorias(vitoriasAdversario)
                .empates(empates)
                .derrotas(vitoriasClube)
                .golsMarcados(golsProAdversario)
                .golsSofridos(golsProClube)
                .build();

        List<PartidaResponseDTO> partidaDTOs = partidas.stream()
                .map(partidaMapper::toDto)
                .collect(Collectors.toList());

        return ConfrontoDiretoResponseDTO.builder()
                .clube(retrospectoClube)
                .adversario(retrospectoAdversario)
                .partidas(partidaDTOs)
                .build();
    }
}
