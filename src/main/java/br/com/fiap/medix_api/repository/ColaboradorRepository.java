package br.com.fiap.medix_api.repository;

import br.com.fiap.medix_api.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    // Retorna todos os colaboradores que não foram deletados
    @Query("SELECT c FROM Colaborador c WHERE c.deleted = 0")
    List<Colaborador> findAllByDeletedIsZero();

    // Retorna todos os colaboradores que foram deletados
    @Query("SELECT c FROM Colaborador c WHERE c.deleted = 1")
    List<Colaborador> findAllByDeletedIsOne();

    // Busca um colaborador por ID, mas apenas se ele não estiver deletado
    Optional<Colaborador> findByIdAndDeletedIs(Long id, Integer deleted);

    @Query("""
        SELECT c FROM Colaborador c
        WHERE c.unidadeSaude.id = :unidadeId
          AND c.especialidade.id = :especialidadeId
          AND c.deleted = 0
          AND NOT EXISTS (
              SELECT a FROM Agendamento a
              WHERE a.colaborador.id = c.id
                AND a.status NOT IN ('CANCELADO_PACIENTE', 'CANCELADO_COLABORADOR')
                AND (a.dataHoraInicio < :fim AND a.dataHoraFim > :inicio)
          )
    """)
    List<Colaborador> findDisponiveisPorHorario(Long unidadeId, Long especialidadeId, LocalDateTime inicio, LocalDateTime fim);
}