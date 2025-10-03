// repository/PacienteRepository.java
package br.com.fiap.medix_api.repository;

import br.com.fiap.medix_api.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findAllByDeletedIs(Integer deleted);
    Optional<Paciente> findByIdAndDeletedIs(Long id, Integer deleted);
}