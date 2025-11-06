package br.com.fiap.medix_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CadastrarSalaDto {
    @NotBlank(message = "O nome da sala é obrigatório.")
    private String nome;
    private String tipo;
    @NotNull(message = "O ID da Unidade de Saúde é obrigatório.")
    private Long idUnidadeSaude;
}