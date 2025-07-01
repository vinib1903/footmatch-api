package com.teamcubation.footmatchapi.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estadios", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nome"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Estadio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

}
