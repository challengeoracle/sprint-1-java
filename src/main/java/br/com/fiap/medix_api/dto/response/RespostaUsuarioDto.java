package br.com.fiap.medix_api.dto.response;

import br.com.fiap.medix_api.enums.UsuarioRole;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class RespostaUsuarioDto extends RepresentationModel<RespostaUsuarioDto> {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private UsuarioRole tipoUsuario;
}