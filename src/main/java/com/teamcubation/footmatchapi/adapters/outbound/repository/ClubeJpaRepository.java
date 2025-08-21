package com.teamcubation.footmatchapi.adapters.outbound.repository;

import com.teamcubation.footmatchapi.adapters.outbound.entities.ClubeJpaEntity;
import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClubeJpaRepository extends JpaRepository<ClubeJpaEntity, Long> {

    Optional<ClubeJpaEntity> findByNomeAndSiglaEstado(String nome, SiglaEstado siglaEstado);

    @Query("SELECT c FROM ClubeJpaEntity c WHERE " +
            "(:nome IS NULL OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:siglaEstado IS NULL OR c.siglaEstado = :siglaEstado) AND " +
            "(:ativo IS NULL OR c.ativo = :ativo)")
    Page<ClubeJpaEntity> findClubesWithFilters(@Param("nome") String nome,
                                      @Param("siglaEstado") SiglaEstado siglaEstado,
                                      @Param("ativo") Boolean ativo,
                                      Pageable pageable);
}