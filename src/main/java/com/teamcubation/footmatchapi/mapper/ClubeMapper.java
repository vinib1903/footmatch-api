package com.teamcubation.footmatchapi.mapper;

import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.dto.response.ClubeResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClubeMapper {
    @Mapping(target = "id", ignore = true)
    Clube toEntity(ClubeRequestDTO clubeRequestDTO);
    ClubeResponseDTO toDto(Clube clube);
}
