package com.teamcubation.footmatchapi.domain.interfaces;

import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClubeRepository {

    Clube save(Clube clube);

    Clube findById(Long id);

    Page<Clube> findClubesWithFilters(String nome, SiglaEstado siglaEstado, Boolean ativo, Pageable pageable);

    Clube findByNomeAndSiglaEstado(String nome, SiglaEstado siglaEstado);

    List<Clube> findAll();


}
