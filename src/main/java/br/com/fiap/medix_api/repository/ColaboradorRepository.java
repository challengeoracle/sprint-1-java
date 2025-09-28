// repository/ColaboradorRepository.java
package br.com.fiap.medix_api.repository;

import br.com.fiap.medix_api.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    // "FROM TB_MEDIX_COLABORADOR WHERE id = 0"
    List<Colaborador> findAllByDeletedIs(Integer deleted);

    // Busca por ID apenas se não estiver deletado
    Optional<Colaborador> findByIdAndDeletedIs(Long id, Integer deleted);
}