package com.teamcubation.footmatchapi.mapper;

import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.dto.response.PartidaResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PartidaMapper {
    Partida toEntity(PartidaRequestDTO partidaRequestDTO);
    PartidaResponseDTO toDto(Partida partida);
}
