package br.com.fiap.medix_api.dto.response;

import br.com.fiap.medix_api.model.UnidadeSaude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class RespostaColaboradorDto extends RespostaUsuarioDto {
    private UnidadeSaude unidadeSaude;
    private String descricaoCargo;
    private String numeroRegistroProfissional;
    private LocalDate dataAdmissao;
}