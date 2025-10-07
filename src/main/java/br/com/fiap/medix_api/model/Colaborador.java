package br.com.fiap.medix_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

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

    @NotNull(message = "A descrição do cargo não pode ser nula.")
    @Size(max = 100, message = "A descrição do cargo não pode exceder 100 caracteres.")
    @Column(name = "ds_cargo", nullable = false, length = 100)
    private String descricaoCargo;

    @Column(name = "nr_registro_profissional", length = 50)
    private String numeroRegistroProfissional;

    @NotNull(message = "A data de admissão não pode ser nula.")
    @Column(name = "dt_admissao", nullable = false)
    private LocalDate dataAdmissao;
}