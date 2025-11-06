package br.com.fiap.medix_api.repository;

import br.com.fiap.medix_api.model.UnidadeSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UnidadeSaudeRepository extends JpaRepository<UnidadeSaude, Long> {

    // Retorna todas as unidades de saúde que não foram deletadas
    @Query("SELECT u FROM UnidadeSaude u WHERE u.deleted = 0")
    List<UnidadeSaude> findAllByDeletedIsZero();

    // Retorna todas as unidades de saúde que foram deletadas
    @Query("SELECT u FROM UnidadeSaude u WHERE u.deleted = 1")
    List<UnidadeSaude> findAllByDeletedIsOne();

    // Busca uma unidade de saúde por ID, mas apenas se ela não estiver deletada
    Optional<UnidadeSaude> findByIdAndDeletedIs(Long id, Integer deleted);

    // Busca uma unidade por CNPJ, para validar unicidade
    @Query("SELECT u FROM UnidadeSaude u WHERE u.cnpj = :cnpj AND u.deleted = 0")
    Optional<UnidadeSaude> findByCnpjAndDeletedIs(String cnpj);

    // Escolhe a unidade que atende essa especialidade
    @Query("SELECT DISTINCT u FROM Colaborador c JOIN c.unidadeSaude u WHERE c.especialidade.id = :especialidadeId AND c.deleted = 0 AND u.deleted = 0")
    List<UnidadeSaude> findByEspecialidadeId(Long especialidadeId);
}