package br.com.fiap.medix_api.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class RespostaColaboradorDto extends RespostaUsuarioDto {
    private RespostaUnidadeSaudeDto unidadeSaude;
    private RespostaEspecialidadeDto especialidade;
    private String descricaoCargo;
    private String numeroRegistroProfissional;
    private LocalDate dataAdmissao;
}