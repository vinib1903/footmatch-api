package com.teamcubation.footmatchapi.adapters.outbound.repository;

import com.teamcubation.footmatchapi.adapters.outbound.entities.ClubeJpaEntity;
import com.teamcubation.footmatchapi.adapters.outbound.entities.EstadioJpaEntity;
import com.teamcubation.footmatchapi.adapters.outbound.entities.PartidaJpaEntity;
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
    Page<PartidaJpaEntity> findPartidasWithFilters(@Param("clube") ClubeJpaEntity clube,
                                          @Param("estadio") EstadioJpaEntity estadio,
                                          @Param("goleada") Boolean goleada,
                                          @Param("papel") String papel,
                                          Pageable pageable);

    @Query("SELECT p FROM PartidaJpaEntity p WHERE " +
            "p.mandante = :clube OR p.visitante = :clube")
    List<PartidaJpaEntity> findAllByClube(@Param("clube") ClubeJpaEntity clube);

    @Query("SELECT p FROM PartidaJpaEntity p WHERE " +
            "(p.mandante = :clube OR p.visitante = :clube) AND " +
            "(p.mandante = :adversario OR p.visitante = :adversario)")
    List<PartidaJpaEntity> findAllByClubes(@Param("clube") ClubeJpaEntity clube,
                                  @Param("adversario") ClubeJpaEntity adversario);

    @Query("SELECT p FROM PartidaJpaEntity p WHERE p.estadio = :estadio AND DATE(p.dataHora) = :data")
    List<PartidaJpaEntity> findAllByEstadioAndData(@Param("estadio") EstadioJpaEntity estadio, @Param("data") LocalDate data);
}
