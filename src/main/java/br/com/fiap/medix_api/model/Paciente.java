package br.com.fiap.medix_api.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "TB_MEDI_PACIENTE")
@PrimaryKeyJoinColumn(name = "id_usuario")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Paciente extends Usuario {

    @Column(name = "dt_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "nr_convenio", length = 50)
    private String numeroConvenio;
}