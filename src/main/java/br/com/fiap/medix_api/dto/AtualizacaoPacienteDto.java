package br.com.fiap.medix_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtualizacaoPacienteDto {
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private String numeroConvenio;
}