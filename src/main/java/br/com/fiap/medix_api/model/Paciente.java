// model/Paciente.java
package br.com.fiap.medix_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "TB_MEDI_PACIENTE")
@PrimaryKeyJoinColumn(name = "id_usuario") // <-- Diz que o ID de Paciente é o mesmo de Usuário
@Data
@SuperBuilder
public class Paciente extends Usuario { // Herda de usuário

    @Column(name = "dt_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "nr_convenio", length = 50)
    private String numeroConvenio;
}