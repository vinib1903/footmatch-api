package com.teamcubation.footmatchapi.domain.entities;

import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "clubes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nome", "siglaEstado"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Clube {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 2)
    private SiglaEstado siglaEstado;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(nullable = false)
    private LocalDate dataCriacao;

}
