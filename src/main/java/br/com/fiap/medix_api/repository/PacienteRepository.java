package br.com.fiap.medix_api.repository;

import br.com.fiap.medix_api.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // Retorna todos os pacientes que não foram deletados
    @Query("SELECT p FROM Paciente p WHERE p.deleted = 0")
    List<Paciente> findAllByDeletedIsZero();

    // Retorna todos os pacientes que foram deletados
    @Query("SELECT p FROM Paciente p WHERE p.deleted = 1")
    List<Paciente> findAllByDeletedIsOne();

    // Busca um paciente por ID, mas apenas se ele não estiver deletado
    Optional<Paciente> findByIdAndDeletedIs(Long id, Integer deleted);

    // Busca um paciente por CPF. Útil para validação
    Optional<Paciente> findByCpf(String cpf);

    // Busca um paciente por e-mail. Útil para validação
    Optional<Paciente> findByEmail(String email);
}