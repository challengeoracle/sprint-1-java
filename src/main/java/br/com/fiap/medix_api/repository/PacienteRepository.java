// repository/PacienteRepository.java
package br.com.fiap.medix_api.repository;

import br.com.fiap.medix_api.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // "FROM TB_MEDIX_PACIENTE WHERE id = 0"
    List<Paciente> findAllByDeletedIs(Integer deleted);

    // Busca por ID apenas se não estiver deletado
    Optional<Paciente> findByIdAndDeletedIs(Long id, Integer deleted);
}