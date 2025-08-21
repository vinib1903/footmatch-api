package com.teamcubation.footmatchapi.adapters.outbound.repository;

import com.teamcubation.footmatchapi.adapters.outbound.entities.ClubeJpaEntity;
import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.domain.interfaces.ClubeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class ClubeRepositoryImpl implements ClubeRepository {

    private final ClubeJpaRepository clubeJpaRepository;

    public ClubeRepositoryImpl(ClubeJpaRepository clubeJpaRepository) {
        this.clubeJpaRepository = clubeJpaRepository;
    }

    @Override
    public Clube save(Clube clube) {
        ClubeJpaEntity clubeJpaEntity = new ClubeJpaEntity(clube);
         this.clubeJpaRepository.save(clubeJpaEntity);
         return new Clube(clubeJpaEntity.getId(), clubeJpaEntity.getNome(), clubeJpaEntity.getSiglaEstado(), clubeJpaEntity.getAtivo(), clubeJpaEntity.getDataCriacao());
    }

    @Override
    public List<Clube> findAll() {
        List<ClubeJpaEntity> clubeJpaEntities = this.clubeJpaRepository.findAll();
        return clubeJpaEntities.stream().map(entity -> new Clube(entity.getId(), entity.getNome(), entity.getSiglaEstado(), entity.getAtivo(), entity.getDataCriacao())).toList();
    }

    @Override
    public Clube findById(Long id) {
        Optional<ClubeJpaEntity> clubeJpaEntity = this.clubeJpaRepository.findById(id);
        return clubeJpaEntity.map(entity -> new Clube(entity.getId(), entity.getNome(), entity.getSiglaEstado(), entity.getAtivo(), entity.getDataCriacao())).orElse(null);
    }

    @Override
    public Page<Clube> findClubesWithFilters(String nome, SiglaEstado siglaEstado, Boolean ativo, Pageable pageable) {
        return this.clubeJpaRepository.findClubesWithFilters(nome, siglaEstado, ativo, pageable);
    }

    @Override
    public Clube findByNomeAndSiglaEstado(String nome, SiglaEstado siglaEstado) {
        Optional<ClubeJpaEntity> clubeJpaEntity = this.clubeJpaRepository.findByNomeAndSiglaEstado(nome, siglaEstado);
        return clubeJpaEntity.map(entity -> new Clube(entity.getId(), entity.getNome(), entity.getSiglaEstado(), entity.getAtivo(), entity.getDataCriacao())).orElse(null);
    }
}
