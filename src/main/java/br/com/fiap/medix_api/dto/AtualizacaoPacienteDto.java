// dto/AtualizacaoPacienteDto.java
package br.com.fiap.medix_api.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AtualizacaoPacienteDto {
    private String nome;

    @Email(message = "Formato de e-mail inválido.")
    private String email;

    private LocalDate dataNascimento;
    private String numeroConvenio;
}