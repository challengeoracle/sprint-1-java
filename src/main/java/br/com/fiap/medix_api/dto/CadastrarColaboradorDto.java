package br.com.fiap.medix_api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CadastrarColaboradorDto extends UsuarioDto {

    // Atributos próprios de Colaborador
    private Long idUnidadeSaude;
    private String cargo;
}