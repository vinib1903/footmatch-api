package com.teamcubation.footmatchapi.adapters.outbound.repository;

import com.teamcubation.footmatchapi.domain.entities.Estadio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    Optional<Estadio> findByNome(String nome);

    Optional<Estadio> findByEndereco_Cep(String cep);

    @Query("SELECT e FROM Estadio e WHERE " +
            "(:nome IS NULL OR LOWER(e.nome) LIKE LOWER(CONCAT('%', :nome, '%')))")
    Page<Estadio> findStadiumsWichFilters(@Param("nome") String nome,
                                      Pageable pageable);
}
