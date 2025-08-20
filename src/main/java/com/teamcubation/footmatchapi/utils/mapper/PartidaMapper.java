package com.teamcubation.footmatchapi.utils.mapper;

import com.teamcubation.footmatchapi.domain.entities.Partida;
import com.teamcubation.footmatchapi.application.dto.request.PartidaRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.PartidaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PartidaMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "mandante", ignore = true)
    @Mapping(target = "visitante", ignore = true)
    @Mapping(target = "estadio", ignore = true)
    Partida toEntity(PartidaRequestDTO partidaRequestDTO);
    PartidaResponseDTO toDto(Partida partida);
}
