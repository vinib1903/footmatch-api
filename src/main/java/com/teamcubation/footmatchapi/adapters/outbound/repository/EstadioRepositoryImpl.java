package com.teamcubation.footmatchapi.adapters.outbound.repository;

import com.teamcubation.footmatchapi.adapters.outbound.entities.EstadioJpaEntity;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.domain.interfaces.EstadioRepository;
import com.teamcubation.footmatchapi.utils.mapper.EstadioMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class EstadioRepositoryImpl implements EstadioRepository {

    private final EstadioJpaRepository estadioJpaRepository;
    private final EstadioMapper estadioMapper;

    public EstadioRepositoryImpl(EstadioJpaRepository estadioJpaRepository, EstadioMapper estadioMapper) {
        this.estadioMapper = estadioMapper;
        this.estadioJpaRepository = estadioJpaRepository;
    }

    @Override
    public Estadio save(Estadio estadio) {
        EstadioJpaEntity estadioJpaEntity = new EstadioJpaEntity(estadio);
        EstadioJpaEntity saved = this.estadioJpaRepository.save(estadioJpaEntity);
        return estadioMapper.jpaToEntity(saved);
    }

    @Override
    public Optional<Estadio>  findByNome(String nome) {
        Optional<EstadioJpaEntity> estadioJpaEntity = this.estadioJpaRepository.findByNome(nome);
        return estadioJpaEntity.map(estadioMapper::jpaToEntity);
    }

    @Override
    public Optional<Estadio> findById(Long id) {
        Optional<EstadioJpaEntity> estadioJpaEntity = this.estadioJpaRepository.findById(id);
        return estadioJpaEntity.map(estadioMapper::jpaToEntity);
    }

    @Override
    public Page<Estadio> findStadiumsWichFilters(String nome, Pageable pageable) {
        Page<EstadioJpaEntity> page = this.estadioJpaRepository.findStadiumsWichFilters(nome, pageable);
        return page.map(estadioMapper::jpaToEntity);
    }

    @Override
    public Optional<Estadio> findByEndereco_Cep(String cep) {
        Optional<EstadioJpaEntity> estadioJpaEntity = this.estadioJpaRepository.findByEndereco_Cep(cep);
        return estadioJpaEntity.map(estadioMapper::jpaToEntity);
    }
}
