package br.com.fiap.medix_api.dto.response;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class RespostaEspecialidadeDto extends RepresentationModel<RespostaEspecialidadeDto> {
    private Long id;
    private String nome;
}