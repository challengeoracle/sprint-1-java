package br.com.fiap.medix_api.repository;

import br.com.fiap.medix_api.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}