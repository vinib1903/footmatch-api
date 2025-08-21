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
        estadioJpaRepository.save(estadioJpaEntity);
        return new Estadio(estadioJpaEntity.getId(), estadioJpaEntity.getNome(), estadioJpaEntity.getEndereco());
    }

    @Override
    public Optional<Estadio>  findByNome(String nome) {
        return this.estadioJpaRepository.findByNome(nome);
    }

    @Override
    public Optional<Estadio> findById(Long id) {
        Optional<EstadioJpaEntity> estadioJpaEntity = this.estadioJpaRepository.findById(id);
        return estadioJpaEntity.map(estadioMapper::jpaToEntity);
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
