package br.com.fiap.medix_api.dto.request;

import br.com.fiap.medix_api.enums.Genero;
import br.com.fiap.medix_api.enums.TipoSanguineo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class AtualizarPacienteDto {
    private String nome;

    @Email(message = "Formato de e-mail inválido.")
    private String email;

    @Past(message = "A data de nascimento não pode ser no futuro.")
    private LocalDate dataNascimento;

    private TipoSanguineo tipoSanguineo;

    private Genero genero;

    private List<@Size(max = 100, message = "O nome da alergia não pode exceder 100 caracteres.")
            String> alergias;
}