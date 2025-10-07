package br.com.fiap.medix_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarUnidadeSaudeDto {
    @NotBlank(message = "O nome não pode ser vazio.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    private String nome;

    @NotBlank(message = "O CNPJ não pode ser vazio.")
    @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter 14 dígitos.")
    private String cnpj;

    @NotBlank(message = "O endereço não pode ser vazio.")
    @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres.")
    private String endereco;

    @Size(max = 15, message = "O telefone deve ter no máximo 15 caracteres.")
    private String telefone;

    @NotBlank(message = "O tipo de unidade não pode ser vazio.")
    @Size(max = 20, message = "O tipo de unidade deve ter no máximo 20 caracteres.")
    private String tipoUnidade;
}