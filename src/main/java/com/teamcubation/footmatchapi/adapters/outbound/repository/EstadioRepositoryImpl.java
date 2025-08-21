package com.teamcubation.footmatchapi.adapters.outbound.repository;

import com.teamcubation.footmatchapi.adapters.outbound.entities.EstadioJpaEntity;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.domain.interfaces.EstadioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class EstadioRepositoryImpl implements EstadioRepository {

    private final EstadioJpaRepository estadioJpaRepository;

    public EstadioRepositoryImpl(EstadioJpaRepository estadioJpaRepository) {
        this.estadioJpaRepository = estadioJpaRepository;
    }

    @Override
    public Estadio save(Estadio estadio) {
        EstadioJpaEntity estadioJpaEntity = new EstadioJpaEntity(estadio);
        estadioJpaRepository.save(estadioJpaEntity);
        return new Estadio(estadioJpaEntity.getId(), estadioJpaEntity.getNome(), estadioJpaEntity.getEndereco());
    }

    @Override
    public Optional<Estadio>  findByNome(String nome) {
        return this.estadioJpaRepository.findByNome(nome);
    }

    @Override
    public Estadio findById(Long id) {
        Optional<EstadioJpaEntity> estadioJpaEntity = this.estadioJpaRepository.findById(id);
        return estadioJpaEntity.map(entity -> new Estadio(entity.getId(), entity.getNome(), entity.getEndereco())).orElse(null);
    }

    @Override
    public Page<Estadio> findStadiumsWichFilters(String nome, Pageable pageable) {
        return this.estadioJpaRepository.findStadiumsWichFilters(nome, pageable);
    }

    @Override
    public Optional<Estadio> findByEndereco_Cep(String cep) {
        return this.estadioJpaRepository.findByEndereco_Cep(cep);
    }
}
