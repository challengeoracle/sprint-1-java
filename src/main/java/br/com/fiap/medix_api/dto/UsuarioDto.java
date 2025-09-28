package br.com.fiap.medix_api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UsuarioDto {
    private String nome;
    private String email;
    private String senha;
    private String cpf;
}