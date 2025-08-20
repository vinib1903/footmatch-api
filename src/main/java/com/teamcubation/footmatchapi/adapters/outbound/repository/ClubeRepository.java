package com.teamcubation.footmatchapi.adapters.outbound.repository;

import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.domain.entities.Clube;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClubeRepository extends JpaRepository<Clube, Long> {

    Optional<Clube> findByNomeAndSiglaEstado(String nome, SiglaEstado siglaEstado);

    @Query("SELECT c FROM Clube c WHERE " +
            "(:nome IS NULL OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:siglaEstado IS NULL OR c.siglaEstado = :siglaEstado) AND " +
            "(:ativo IS NULL OR c.ativo = :ativo)")
    Page<Clube> findClubesWithFilters(@Param("nome") String nome,
                                      @Param("siglaEstado") SiglaEstado siglaEstado,
                                      @Param("ativo") Boolean ativo,
                                      Pageable pageable);
}