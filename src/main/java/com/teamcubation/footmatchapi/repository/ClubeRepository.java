package com.teamcubation.footmatchapi.repository;

import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.domain.entities.Clube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface ClubeRepository extends JpaRepository<Clube, Long>, JpaSpecificationExecutor<Clube> {

    Optional<Clube> findByNomeAndSiglaEstado(String nome, SiglaEstado siglaEstado);
}