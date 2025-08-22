package com.teamcubation.footmatchapi.adapters.outbound.repository;

import com.teamcubation.footmatchapi.adapters.outbound.entities.ClubeJpaEntity;
import com.teamcubation.footmatchapi.adapters.outbound.entities.EstadioJpaEntity;
import com.teamcubation.footmatchapi.adapters.outbound.entities.PartidaJpaEntity;
import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.application.ports.out.PartidaRepository;
import com.teamcubation.footmatchapi.utils.mapper.PartidaMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PartidaRepositoryImpl implements PartidaRepository {

    private final PartidaJpaRepository partidaJpaRepository;
    private final PartidaMapper partidaMapper;

    public PartidaRepositoryImpl(PartidaJpaRepository partidaJpaRepository, PartidaMapper partidaMapper) {
        this.partidaMapper = partidaMapper;
        this.partidaJpaRepository = partidaJpaRepository;
    }

    @Override
    public Page<Partida> findPartidasWithFilters(Clube clube, Estadio estadio, Boolean goleada, String papel, Pageable pageable) {
        ClubeJpaEntity clubeJpa = (clube != null) ? new ClubeJpaEntity(clube) : null;
        EstadioJpaEntity estadioJpa = (estadio != null) ? new EstadioJpaEntity(estadio) : null;
        Page<PartidaJpaEntity> page = this.partidaJpaRepository.findPartidasWithFilters(clubeJpa, estadioJpa, goleada, papel, pageable);
        return page.map(partidaMapper::jpaToEntity);
    }

    @Override
    public List<Partida> findAllByClube(Clube clube) {
        ClubeJpaEntity clubeJpa = (clube != null) ? new ClubeJpaEntity(clube) : null;
        List<PartidaJpaEntity> list = this.partidaJpaRepository.findAllByClube(clubeJpa);
        return list.stream().map(partidaMapper::jpaToEntity).collect(Collectors.toList());
    }

    @Override
    public List<Partida> findAllByClubes(Clube clube, Clube adversario) {
        ClubeJpaEntity clubeJpa = new ClubeJpaEntity(clube);
        ClubeJpaEntity adversarioJpa = new ClubeJpaEntity(adversario);
        List<PartidaJpaEntity> list = this.partidaJpaRepository.findAllByClubes(clubeJpa, adversarioJpa);
        return list.stream().map(partidaMapper::jpaToEntity).collect(Collectors.toList());
    }

    @Override
    public List<Partida> findAllByEstadioAndData(Estadio estadio, LocalDate data) {
        EstadioJpaEntity estadioJpa = new EstadioJpaEntity(estadio);
        List<PartidaJpaEntity> list = this.partidaJpaRepository.findAllByEstadioAndData(estadioJpa, data);
        return list.stream().map(partidaMapper::jpaToEntity).collect(Collectors.toList());
    }

    @Override
    public Partida save(Partida partida) {
        PartidaJpaEntity partidaJpaEntity = new PartidaJpaEntity(partida);
        PartidaJpaEntity savedEntity = partidaJpaRepository.save(partidaJpaEntity);
        return partidaMapper.jpaToEntity(savedEntity);
    }

    @Override
    public Optional<Partida> findById(Long id) {
        Optional<PartidaJpaEntity> partidaJpaEntity = this.partidaJpaRepository.findById(id);
        return partidaJpaEntity.map(partidaMapper::jpaToEntity);
    }

    public void deletePartida(Long id) {
        this.partidaJpaRepository.deleteById(id);
    }
}
