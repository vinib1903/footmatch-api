package com.teamcubation.footmatchapi.repository;

import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

    @Query("SELECT COUNT(p) > 0 FROM Partida p WHERE " +
           "(p.mandante = :clube OR p.visitante = :clube) AND " +
           "p.dataHora >= :dataLimite")
    boolean existePartidaPosteriorData(@Param("clube") Clube clube,
                                      @Param("dataLimite") LocalDateTime dataLimite);
}
