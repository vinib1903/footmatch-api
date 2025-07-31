package com.teamcubation.footmatchapi.domain.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "partidas")
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da partida.", example = "1")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mandante_id")
    @Schema(description = "Identificador único do clube mandante da partida.", example = "1", required = true)
    private Clube mandante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visitante_id")
    @Schema(description = "Identificador único do clube visitante da partida.", example = "2", required = true)
    private Clube visitante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estadio_id")
    @Schema(description = "Identificador único do estádio da partida.", example = "1", required = true)
    private Estadio estadio;

    @Column(nullable = false)
    @Schema(description = "Data e hora da partida.", example = "2023-09-15T12:00:00", format = "yyyy-MM-dd'T'HH:mm:ss", required = true)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    @Schema(description = "Número de gols do clube mandante.", example = "3", required = true)
    private Integer golsMandante;

    @Column(nullable = false)
    @Schema(description = "Número de gols do clube visitante.", example = "0", required = true)
    private Integer golsVisitante;

}
