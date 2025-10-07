package br.com.fiap.medix_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarColaboradorDto {

    private String nome;

    @Email(message = "Formato de e-mail inválido.")
    private String email;

    @Size(max = 100, message = "O cargo deve ter no máximo 100 caracteres.")
    private String descricaoCargo;

    @Size(max = 50, message = "O número de registro profissional deve ter no máximo 50 caracteres.")
    private String numeroRegistroProfissional;

    private LocalDate dataAdmissao;
}