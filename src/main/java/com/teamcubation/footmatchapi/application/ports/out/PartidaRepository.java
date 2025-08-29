package com.teamcubation.footmatchapi.application.ports.out;

import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PartidaRepository {

    Partida save(Partida partida);

    Optional<Partida> findById(Long id);

    Page<Partida> findPartidasWithFilters(Clube clube, Estadio estadio, Boolean goleada, String papel, Pageable pageable);

    List<Partida> findAllByClube(Clube clube);

    List<Partida> findAllByClubes(Clube clube, Clube adversario);

    List<Partida> findAllByEstadioAndData(Estadio estadio, LocalDate data);

    void deletePartida(Long id);
}
