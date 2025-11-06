package br.com.fiap.medix_api.repository;

import br.com.fiap.medix_api.enums.StatusAgendamento;
import br.com.fiap.medix_api.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Listagens gerais
    List<Agendamento> findByPacienteId(Long pacienteId);
    List<Agendamento> findByColaboradorId(Long colaboradorId);

    // NOVO: Busca a PRÓXIMA consulta de um paciente (a primeira que estiver no futuro e ativa)
    Optional<Agendamento> findFirstByPacienteIdAndDataHoraInicioAfterAndStatusInOrderByDataHoraInicioAsc(
            Long pacienteId,
            LocalDateTime dataHora,
            List<StatusAgendamento> statusValidos
    );

    // Validadores de conflito (já existiam)
    @Query("SELECT COUNT(a) > 0 FROM Agendamento a " +
            "WHERE a.colaborador.id = :colaboradorId " +
            "AND a.status NOT IN ('CANCELADO_PACIENTE', 'CANCELADO_COLABORADOR') " +
            "AND (a.dataHoraInicio < :fim AND a.dataHoraFim > :inicio)")
    boolean existsConflitoColaborador(@Param("colaboradorId") Long colaboradorId,
                                      @Param("inicio") LocalDateTime inicio,
                                      @Param("fim") LocalDateTime fim);

    @Query("SELECT COUNT(a) > 0 FROM Agendamento a " +
            "WHERE a.paciente.id = :pacienteId " +
            "AND a.status NOT IN ('CANCELADO_PACIENTE', 'CANCELADO_COLABORADOR') " +
            "AND (a.dataHoraInicio < :fim AND a.dataHoraFim > :inicio)")
    boolean existsConflitoPaciente(@Param("pacienteId") Long pacienteId,
                                   @Param("inicio") LocalDateTime inicio,
                                   @Param("fim") LocalDateTime fim);

    @Query("SELECT COUNT(a) > 0 FROM Agendamento a " +
            "WHERE a.sala.id = :salaId " +
            "AND a.status NOT IN ('CANCELADO_PACIENTE', 'CANCELADO_COLABORADOR') " +
            "AND (a.dataHoraInicio < :fim AND a.dataHoraFim > :inicio)")
    boolean existsConflitoSala(@Param("salaId") Long salaId,
                               @Param("inicio") LocalDateTime inicio,
                               @Param("fim") LocalDateTime fim);
}