package br.com.fiap.medix_api.repository;

import br.com.fiap.medix_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Retorna todos os usuários com o status de deletado específico
    List<Usuario> findAllByDeletedIs(Integer deleted);

    // Busca um usuário por ID, mas apenas se ele não estiver deletado
    Optional<Usuario> findByIdAndDeletedIs(Long id, Integer deleted);

    // Busca um usuário por e-mail, para validação ou login, apenas se ele estiver ativo
    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.deleted = 0")
    Optional<Usuario> findByEmailAndDeletedIs(String email);

    // Busca um usuário por CPF, para validação, apenas se ele estiver ativo
    @Query("SELECT u FROM Usuario u WHERE u.cpf = :cpf AND u.deleted = 0")
    Optional<Usuario> findByCpfAndDeletedIs(String cpf);
}