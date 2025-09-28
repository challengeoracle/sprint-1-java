package br.com.fiap.medix_api.dto;

import br.com.fiap.medix_api.model.Paciente;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true) // Importante para herança
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CadastroPacienteDto extends UsuarioDto {

    // Atributos próprios de Paciente
    private LocalDate dataNascimento;
    private String numeroConvenio;
}