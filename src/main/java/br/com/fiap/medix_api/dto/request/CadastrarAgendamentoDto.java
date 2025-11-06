package br.com.fiap.medix_api.dto.request;

import br.com.fiap.medix_api.enums.TipoAgendamento;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CadastrarAgendamentoDto {

    @NotNull(message = "O ID do paciente é obrigatório.")
    private Long idPaciente;

    @NotNull(message = "O ID do colaborador é obrigatório.")
    private Long idColaborador;

    @NotNull(message = "O ID da unidade de saúde é obrigatório.")
    private Long idUnidadeSaude;

    private Long idSala; // Opcional no cadastro inicial

    @NotNull(message = "A data e hora de início são obrigatórias.")
    @Future(message = "O agendamento deve ser para uma data futura.")
    private LocalDateTime dataHoraInicio;

    @NotNull(message = "O tipo de agendamento é obrigatório.")
    private TipoAgendamento tipo;

    private String observacoes;
}