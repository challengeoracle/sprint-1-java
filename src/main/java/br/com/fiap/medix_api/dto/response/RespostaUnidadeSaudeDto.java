package br.com.fiap.medix_api.dto.response;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class RespostaUnidadeSaudeDto extends RepresentationModel<RespostaUnidadeSaudeDto> {
    private Long id;
    private String nome;
    private String endereco;
    private String tipoUnidade;
}