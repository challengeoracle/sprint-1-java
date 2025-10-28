package br.com.fiap.medix_api.repository;

import br.com.fiap.medix_api.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    // Retorna todas as avaliações que não foram deletadas
    @Query("SELECT a FROM Avaliacao a WHERE a.deleted = 0") //
    List<Avaliacao> findAllByDeletedIsZero(); //

    // Retorna todas as avaliações que foram deletadas
    @Query("SELECT a FROM Avaliacao a WHERE a.deleted = 1") //
    List<Avaliacao> findAllByDeletedIsOne(); //

    // Busca uma avaliação por ID, mas apenas se ela não estiver deletada (deleted = 0)
    Optional<Avaliacao> findByIdAndDeletedIs(Long id, Integer deleted); //
}