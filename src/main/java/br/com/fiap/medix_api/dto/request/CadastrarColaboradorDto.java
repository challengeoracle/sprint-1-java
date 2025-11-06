package br.com.fiap.medix_api.dto.request;

import br.com.fiap.medix_api.dto.UsuarioDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
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
public class CadastrarColaboradorDto extends UsuarioDto {

    @NotNull(message = "O ID da unidade de saúde não pode ser nulo.")
    private Long idUnidadeSaude;

    private Long idEspecialidade;

    @NotBlank(message = "O cargo não pode ser vazio.")
    @Size(max = 100, message = "O cargo deve ter no máximo 100 caracteres.")
    private String descricaoCargo;

    @Size(max = 50, message = "O número de registro profissional deve ter no máximo 50 caracteres.")
    private String numeroRegistroProfissional;

    @NotNull(message = "A data de admissão não pode ser nula.")
    @PastOrPresent(message = "A data de admissão não pode ser no futuro.")
    private LocalDate dataAdmissao;
}