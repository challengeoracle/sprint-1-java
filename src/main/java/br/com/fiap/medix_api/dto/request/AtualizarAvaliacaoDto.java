package br.com.fiap.medix_api.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarAvaliacaoDto {

    // Permite atualizar o horário, mantendo a validação
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$", message = "Formato de horário inválido. Use HH:MM:SS.")
    private String horario; // Continuamos recebendo como String

    @Size(max = 100, message = "O setor deve ter no máximo 100 caracteres.")
    private String setor;

    @Size(max = 100, message = "O local deve ter no máximo 100 caracteres.")
    private String local;

    @Size(max = 20, message = "A avaliação deve ter no máximo 20 caracteres.")
    private String avaliacao;
}