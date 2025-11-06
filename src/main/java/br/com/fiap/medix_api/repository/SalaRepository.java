package br.com.fiap.medix_api.repository;

import br.com.fiap.medix_api.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    // Graças ao @Where na entidade, o findAll padrão já filtra os deletados.
    List<Sala> findByUnidadeSaudeId(Long idUnidadeSaude);
}