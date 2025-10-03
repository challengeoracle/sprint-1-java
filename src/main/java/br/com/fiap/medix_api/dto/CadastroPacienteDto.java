// dto/CadastroPacienteDto.java
package br.com.fiap.medix_api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CadastroPacienteDto extends UsuarioDto {
    private LocalDate dataNascimento;
    private String numeroConvenio;
}