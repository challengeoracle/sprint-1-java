package br.com.fiap.medix_api.repository;

import br.com.fiap.medix_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
