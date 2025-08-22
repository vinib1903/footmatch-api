package com.teamcubation.footmatchapi.adapters.outbound.entities;

import com.teamcubation.footmatchapi.domain.entities.Estadio;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estadios", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nome"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadioJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do estadio.", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nome do estadio.", example = "Arena do Grêmio", required = true)
    private String nome;

    @Embedded
    @Schema(description = "Endereço do estadio, gerado automaticamente no ato do cadastro.", required = true)
    private EnderecoJpaEntity endereco;

    public EstadioJpaEntity(Estadio estadio) {
        this.id = estadio.getId();
        this.nome = estadio.getNome();
        this.endereco = new EnderecoJpaEntity(estadio.getEndereco());
    }
}
