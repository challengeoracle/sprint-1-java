package br.com.fiap.medix_api.dto.request;

import br.com.fiap.medix_api.dto.UsuarioDto;
import br.com.fiap.medix_api.enums.Genero;
import br.com.fiap.medix_api.enums.TipoSanguineo;
import br.com.fiap.medix_api.model.Paciente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CadastrarPacienteDto extends UsuarioDto {
    @NotNull(message = "A data de nascimento não pode ser nula.")
    @Past(message = "A data de nascimento não pode ser no futuro.")
    private LocalDate dataNascimento;

    @NotNull(message = "O tipo sanguíneo não pode ser nulo.")
    private TipoSanguineo tipoSanguineo;

    @NotNull(message = "O gênero não pode ser nulo.")
    private Genero genero;

    // Lista de alergias, pode ser opcional. O @Size valida cada item.
    private List<@NotBlank(message = "A alergia não pode ser vazia.")
    @Size(max = 100, message = "O nome da alergia não pode exceder 100 caracteres.")
            String> alergias;
}