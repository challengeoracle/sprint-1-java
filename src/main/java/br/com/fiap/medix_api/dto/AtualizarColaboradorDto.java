package br.com.fiap.medix_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarColaboradorDto {

    // Apenas os dados que podem ser atualizados
    private String nome;
    private String email;
    private String cargo;
}