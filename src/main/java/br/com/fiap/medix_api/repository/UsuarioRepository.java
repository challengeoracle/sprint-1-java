package br.com.fiap.medix_api.repository;

import br.com.fiap.medix_api.model.Paciente;
import br.com.fiap.medix_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // "FROM TB_MEDIX_PACIENTE WHERE id = 0"
    List<Usuario> findAllByDeletedIs(Integer deleted);

    // Busca por ID apenas se não estiver deletado
    Optional<Usuario> findByIdAndDeletedIs(Long id, Integer deleted);
}
