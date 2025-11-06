package br.com.fiap.medix_api.service;

import br.com.fiap.medix_api.dto.request.CadastrarSalaDto;
import br.com.fiap.medix_api.model.Sala;
import br.com.fiap.medix_api.model.UnidadeSaude;
import br.com.fiap.medix_api.repository.SalaRepository;
import br.com.fiap.medix_api.repository.UnidadeSaudeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaService {

    private final SalaRepository salaRepository;
    private final UnidadeSaudeRepository unidadeSaudeRepository;

    @Transactional
    public Sala criar(CadastrarSalaDto dto) {
        UnidadeSaude unidade = unidadeSaudeRepository.findByIdAndDeletedIs(dto.getIdUnidadeSaude(), 0)
                .orElseThrow(() -> new EntityNotFoundException("Unidade de Saúde não encontrada ou inativa."));

        Sala sala = new Sala();
        sala.setNome(dto.getNome());
        sala.setTipo(dto.getTipo());
        sala.setUnidadeSaude(unidade);

        return salaRepository.save(sala);
    }

    public List<Sala> listarPorUnidade(Long idUnidade) {
        return salaRepository.findByUnidadeSaudeId(idUnidade);
    }

    public Sala buscarPorId(Long id) {
        return salaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sala não encontrada com ID: " + id));
    }

    @Transactional
    public void excluir(Long id) {
        salaRepository.deleteById(id); // O @SQLDelete na entidade fará o soft delete
    }
}