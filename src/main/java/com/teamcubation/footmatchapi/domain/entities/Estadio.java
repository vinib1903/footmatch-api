package com.teamcubation.footmatchapi.domain.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estadios", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nome"})
})
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Estadio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do estadio.", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nome do estadio.", example = "Arena do Grêmio", required = true)
    private String nome;

    @Embedded
    @Schema(description = "Endereço do estadio, gerado automaticamente no ato do cadastro.", required = true)
    private Endereco endereco;
}
