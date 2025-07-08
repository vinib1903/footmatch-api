package com.teamcubation.footmatchapi.repository;

import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.dto.response.PartidaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

    @Query("SELECT COUNT(p) > 0 FROM Partida p WHERE " +
           "(p.mandante = :clube OR p.visitante = :clube) AND " +
           "p.dataHora >= :dataLimite")
    boolean existsPartidaAfterDate(@Param("clube") Clube clube,
                                      @Param("dataLimite") LocalDateTime dataLimite);

    @Query("SELECT p FROM Partida p WHERE " +
           "(:clube IS NULL OR p.mandante = :clube OR p.visitante = :clube) AND " +
           "(:estadio IS NULL OR p.estadio = :estadio)")
    Page<Partida> findPartidasWithFilters(@Param("clube") Clube clube,
                                                    @Param("estadio") Estadio estadio,
                                                    Pageable pageable);

    @Query("SELECT COUNT(p) > 0 FROM Partida p WHERE " +
            "(p.mandante = :clube OR p.visitante = :clube) AND " +
            "p.dataHora BETWEEN :inicio AND :fim")
    boolean existsByClubeAndDataHoraBetween(@Param("clube") Clube clube,
                                            @Param("inicio") LocalDateTime inicio,
                                            @Param("fim") LocalDateTime fim);

    @Query("SELECT COUNT(p) > 0 FROM Partida p WHERE " +
            "p.estadio = :estadio AND DATE(p.dataHora) = :data")
    boolean existsByEstadioAndData(@Param("estadio") Estadio estadio,
                                   @Param("data") LocalDate data);

    @Query("SELECT p FROM Partida p WHERE " +
            "p.mandante = :clube OR p.visitante = :clube")
    List<Partida> FindAllByClube(@Param("clube") Clube clube);
}
