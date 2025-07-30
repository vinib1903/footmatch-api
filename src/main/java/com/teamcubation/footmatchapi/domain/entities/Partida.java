package com.teamcubation.footmatchapi.domain.entities;

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
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mandante_id")
    private Clube mandante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visitante_id")
    private Clube visitante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estadio_id")
    private Estadio estadio;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private Integer golsMandante;

    @Column(nullable = false)
    private Integer golsVisitante;

}
