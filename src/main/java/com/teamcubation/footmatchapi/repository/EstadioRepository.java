package com.teamcubation.footmatchapi.repository;

import com.teamcubation.footmatchapi.domain.entities.Estadio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadioRepository extends JpaRepository<Estadio, Long> {


    Optional<Estadio> findByNome(@NotBlank(message = "O nome do estádio é obrigatório.") @Size(min = 3, message = "O nome deve conter ao menos 3 caracteres.") String nome);
}
