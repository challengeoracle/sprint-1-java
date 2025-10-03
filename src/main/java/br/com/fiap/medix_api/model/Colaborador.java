package br.com.fiap.medix_api.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "TB_MEDI_COLABORADOR")
@PrimaryKeyJoinColumn(name = "id_usuario")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Colaborador extends Usuario {

    @ManyToOne
    @JoinColumn(name = "id_unidade_saude", nullable = false)
    private UnidadeSaude unidadeSaude;

    @Column(name = "ds_cargo", nullable = false, length = 50)
    private String cargo;
}