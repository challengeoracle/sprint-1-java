package br.com.fiap.medix_api.dto.response;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class RespostaSalaDto extends RepresentationModel<RespostaSalaDto> {
    private Long id;
    private String nome;
    private String tipo;
    private Long idUnidadeSaude;
    private String nomeUnidadeSaude;
}