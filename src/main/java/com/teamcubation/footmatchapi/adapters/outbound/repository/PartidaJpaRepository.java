package com.teamcubation.footmatchapi.adapters.outbound.repository;

import com.teamcubation.footmatchapi.adapters.outbound.entities.PartidaJpaEntity;
import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PartidaJpaRepository extends JpaRepository<PartidaJpaEntity, Long> {

    @Query("SELECT p FROM PartidaJpaEntity p WHERE " +
            "(:clube IS NULL OR " +
            "(:papel = 'mandante' AND p.mandante = :clube) OR " +
            "(:papel = 'visitante' AND p.visitante = :clube) OR " +
            "(:papel IS NULL AND (p.mandante = :clube OR p.visitante = :clube))) AND " +
            "(:estadio IS NULL OR p.estadio = :estadio) AND " +
            "(:goleada = TRUE AND ABS(p.golsMandante - p.golsVisitante) >= 3 OR :goleada IS NULL OR :goleada = FALSE)")
    Page<Partida> findPartidasWithFilters(@Param("clube") Clube clube,
                                          @Param("estadio") Estadio estadio,
                                          @Param("goleada") Boolean goleada,
                                          @Param("papel") String papel,
                                          Pageable pageable);

    @Query("SELECT p FROM Partida p WHERE " +
            "p.mandante = :clube OR p.visitante = :clube")
    List<Partida> findAllByClube(@Param("clube") Clube clube);

    @Query("SELECT p FROM Partida p WHERE " +
            "(p.mandante = :clube OR p.visitante = :clube) AND " +
            "(p.mandante = :adversario OR p.visitante = :adversario)")
    List<Partida> findAllByClubes(@Param("clube") Clube clube,
                                  @Param("adversario") Clube adversario);

    @Query("SELECT p FROM Partida p WHERE p.estadio = :estadio AND DATE(p.dataHora) = :data")
    List<Partida> findAllByEstadioAndData(@Param("estadio") Estadio estadio, @Param("data") LocalDate data);
}
