package com.teamcubation.footmatchapi.mapper;

import com.teamcubation.footmatchapi.domain.entities.Endereco;
import com.teamcubation.footmatchapi.dto.response.EnderecoResponseDTO;
import com.teamcubation.footmatchapi.dto.response.ViaCepResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface EnderecoMapper {

    Endereco fromViaCepResponse(ViaCepResponseDTO response);
    EnderecoResponseDTO toDto(Endereco endereco);
}
