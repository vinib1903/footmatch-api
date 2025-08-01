package com.teamcubation.footmatchapi.domain.entities;

import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "clubes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nome", "siglaEstado"})
})
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@Schema(description = "Representa um clube de futebol existente ou não no mundo real.")
public class Clube {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do clube.", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nome do clube.", example = "Grêmio", required = true)
    private String nome;

    @Column(nullable = false, length = 2)
    @Schema(description = "Sigla do estado do clube.", example = "RS", required = true, allowableValues = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"})
    private SiglaEstado siglaEstado;

    @Column(nullable = false)
    @Schema(description = "Indica se o clube está ativo ou inativo.", example = "true", required = true)
    private Boolean ativo;

    @Column(nullable = false)
    @Schema(description = "Data de criação do clube.", example = "1903-09-15", format = "yyyy-MM-dd", required = true)
    private LocalDate dataCriacao;

}
