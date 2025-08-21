package com.teamcubation.footmatchapi.adapters.outbound.repository;

import com.teamcubation.footmatchapi.adapters.outbound.entities.PartidaJpaEntity;
import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.domain.interfaces.PartidaRepository;
import com.teamcubation.footmatchapi.utils.mapper.PartidaMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        return this.partidaJpaRepository.findPartidasWithFilters(clube, estadio, goleada, papel, pageable);
    }

    @Override
    public List<Partida> findAllByClube(Clube clube) {
        return this.partidaJpaRepository.findAllByClube(clube);
    }

    @Override
    public List<Partida> findAllByClubes(Clube clube, Clube adversario) {
        return this.partidaJpaRepository.findAllByClubes(clube, adversario);
    }

    @Override
    public List<Partida> findAllByEstadioAndData(Estadio estadio, LocalDate data) {
        return this.partidaJpaRepository.findAllByEstadioAndData(estadio, data);
    }

    @Override
    public Partida save(Partida partida) {
        PartidaJpaEntity partidaJpaEntity = new PartidaJpaEntity(partida);
        partidaJpaRepository.save(partidaJpaEntity);
        return new Partida(partidaJpaEntity.getId(), partidaJpaEntity.getMandante(), partidaJpaEntity.getVisitante(), partidaJpaEntity.getEstadio(), partidaJpaEntity.getDataHora(), partidaJpaEntity.getGolsMandante(), partidaJpaEntity.getGolsVisitante());
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
