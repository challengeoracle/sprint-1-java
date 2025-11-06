package br.com.fiap.medix_api.dto.response;

import br.com.fiap.medix_api.enums.StatusAgendamento;
import br.com.fiap.medix_api.enums.TipoAgendamento;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
public class RespostaAgendamentoDto extends RepresentationModel<RespostaAgendamentoDto> {
    private Long id;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private TipoAgendamento tipo;
    private StatusAgendamento status;
    private String observacoes;

    // Dados resumidos dos relacionamentos
    private Long idPaciente;
    private String nomePaciente;
    private Long idColaborador;
    private String nomeColaborador;
    private Long idUnidadeSaude;
    private String nomeUnidadeSaude;
    private Long idSala;
    private String nomeSala;
}