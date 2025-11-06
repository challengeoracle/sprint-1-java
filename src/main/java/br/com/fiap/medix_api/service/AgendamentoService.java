package br.com.fiap.medix_api.service;

import br.com.fiap.medix_api.dto.request.CadastrarAgendamentoDto;
import br.com.fiap.medix_api.enums.StatusAgendamento;
import br.com.fiap.medix_api.model.*;
import br.com.fiap.medix_api.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteRepository pacienteRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final UnidadeSaudeRepository unidadeSaudeRepository;
    private final SalaRepository salaRepository;

    // ... (Mantenha o método 'agendar' e 'validarDisponibilidade' como estavam) ...
    // Vou omiti-los aqui para economizar espaço, mas você já tem eles do passo anterior.
    // Se precisar que eu reenvie tudo completo, me avise.

    @Transactional
    public Agendamento agendar(CadastrarAgendamentoDto dto) {
        // ... (seu código de agendar existente) ...
        // Só para constar: certifique-se de que ele está aqui
        Paciente paciente = pacienteRepository.findByIdAndDeletedIs(dto.getIdPaciente(), 0)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado."));
        Colaborador colaborador = colaboradorRepository.findByIdAndDeletedIs(dto.getIdColaborador(), 0)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado."));
        UnidadeSaude unidade = unidadeSaudeRepository.findByIdAndDeletedIs(dto.getIdUnidadeSaude(), 0)
                .orElseThrow(() -> new EntityNotFoundException("Unidade de Saúde não encontrada."));

        Sala sala = null;
        if (dto.getIdSala() != null) {
            sala = salaRepository.findById(dto.getIdSala())
                    .orElseThrow(() -> new EntityNotFoundException("Sala não encontrada."));
            if (!sala.getUnidadeSaude().getId().equals(unidade.getId())) {
                throw new IllegalArgumentException("A sala informada não pertence a esta unidade de saúde.");
            }
        }

        LocalDateTime inicio = dto.getDataHoraInicio();
        LocalDateTime fim = inicio.plusMinutes(dto.getTipo().getDuracaoPadraoMinutos());

        validarDisponibilidade(colaborador.getId(), paciente.getId(), (sala != null ? sala.getId() : null), inicio, fim);

        Agendamento agendamento = Agendamento.builder()
                .paciente(paciente)
                .colaborador(colaborador)
                .unidadeSaude(unidade)
                .sala(sala)
                .dataHoraInicio(inicio)
                .dataHoraFim(fim)
                .tipo(dto.getTipo())
                .status(StatusAgendamento.AGENDADO)
                .observacoes(dto.getObservacoes())
                .build();

        return agendamentoRepository.save(agendamento);
    }

    private void validarDisponibilidade(Long colabId, Long pacId, Long salaId, LocalDateTime inicio, LocalDateTime fim) {
        if (agendamentoRepository.existsConflitoColaborador(colabId, inicio, fim)) {
            throw new IllegalStateException("O colaborador já possui agendamento neste horário.");
        }
        if (agendamentoRepository.existsConflitoPaciente(pacId, inicio, fim)) {
            throw new IllegalStateException("O paciente já possui agendamento neste horário.");
        }
        if (salaId != null && agendamentoRepository.existsConflitoSala(salaId, inicio, fim)) {
            throw new IllegalStateException("A sala já está ocupada neste horário.");
        }
    }

    public List<Agendamento> listarPorPaciente(Long pacienteId) {
        return agendamentoRepository.findByPacienteId(pacienteId);
    }

    public List<Agendamento> listarPorColaborador(Long colaboradorId) {
        return agendamentoRepository.findByColaboradorId(colaboradorId);
    }

    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado."));
    }

    // NOVO: Busca a próxima consulta
    public Optional<Agendamento> buscarProximaConsultaPaciente(Long pacienteId) {
        // Consideramos "próxima" apenas as que estão AGENDADO ou CONFIRMADO
        List<StatusAgendamento> statusValidos = List.of(StatusAgendamento.AGENDADO, StatusAgendamento.CONFIRMADO);
        return agendamentoRepository.findFirstByPacienteIdAndDataHoraInicioAfterAndStatusInOrderByDataHoraInicioAsc(
                pacienteId, LocalDateTime.now(), statusValidos
        );
    }

    @Transactional
    public Agendamento atualizarStatus(Long id, StatusAgendamento novoStatus) {
        Agendamento agendamento = buscarPorId(id);
        // Regra básica: não pode reativar um cancelado/realizado (opcional)
        if (List.of(StatusAgendamento.CANCELADO_PACIENTE, StatusAgendamento.CANCELADO_COLABORADOR, StatusAgendamento.REALIZADO)
                .contains(agendamento.getStatus())) {
            throw new IllegalStateException("Não é possível alterar o status de um agendamento já finalizado ou cancelado.");
        }
        agendamento.setStatus(novoStatus);
        return agendamentoRepository.save(agendamento);
    }

    // NOVO: Atalhos para cancelamento
    @Transactional
    public Agendamento cancelarPorPaciente(Long id) {
        return atualizarStatus(id, StatusAgendamento.CANCELADO_PACIENTE);
    }

    @Transactional
    public Agendamento cancelarPorColaborador(Long id) {
        return atualizarStatus(id, StatusAgendamento.CANCELADO_COLABORADOR);
    }
}