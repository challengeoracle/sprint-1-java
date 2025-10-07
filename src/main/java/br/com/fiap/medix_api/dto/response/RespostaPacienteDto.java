package br.com.fiap.medix_api.dto.response;

import br.com.fiap.medix_api.enums.Genero;
import br.com.fiap.medix_api.enums.TipoSanguineo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RespostaPacienteDto extends RespostaUsuarioDto {
    private LocalDate dataNascimento;
    private TipoSanguineo tipoSanguineo;
    private Genero genero;
    private List<String> alergias;
}