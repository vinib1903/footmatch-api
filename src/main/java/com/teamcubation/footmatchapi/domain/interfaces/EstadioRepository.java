package com.teamcubation.footmatchapi.domain.interfaces;

import com.teamcubation.footmatchapi.domain.entities.Estadio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EstadioRepository {

    Estadio save(Estadio estadio);

    Optional<Estadio> findByNome(String nome);

    Optional<Estadio> findById(Long id);

    Page<Estadio> findStadiumsWichFilters(String nome, Pageable pageable);

    Optional<Estadio> findByEndereco_Cep(String cep);
}
