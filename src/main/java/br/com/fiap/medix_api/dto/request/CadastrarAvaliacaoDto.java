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
public class CadastrarAvaliacaoDto {

    // Valida o formato HH:MM:SS
    @NotBlank(message = "O horário não pode ser vazio.")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$", message = "Formato de horário inválido. Use HH:MM:SS.")
    private String horario; // Vem como String do JSON

    @NotBlank(message = "O setor não pode ser vazio.")
    @Size(max = 100)
    private String setor;

    @NotBlank(message = "O local não pode ser vazio.")
    @Size(max = 100)
    private String local;

    @NotBlank(message = "A avaliação não pode ser vazia.")
    @Size(max = 20)
    private String avaliacao;

    // Ignoraremos "ID" e "Sensor" vindos do ESP32 por enquanto,
    // mas poderiam ser adicionados aqui se necessário.
}