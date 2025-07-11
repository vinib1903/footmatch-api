package com.teamcubation.footmatchapi.repository;

import com.teamcubation.footmatchapi.domain.entities.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    Optional<Estadio> findByNome(String nome);
}
