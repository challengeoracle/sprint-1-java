package br.com.fiap.medix_api.service;

import br.com.fiap.medix_api.dto.request.AtualizarAvaliacaoDto;
import br.com.fiap.medix_api.dto.request.CadastrarAvaliacaoDto;
import br.com.fiap.medix_api.model.Avaliacao;
import br.com.fiap.medix_api.repository.AvaliacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class AvaliacaoService {

    private AvaliacaoRepository avaliacaoRepository;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

    @Transactional
    public Avaliacao registrarAvaliacao(CadastrarAvaliacaoDto dto) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setHorarioAvaliacao(LocalTime.parse(dto.getHorario(), timeFormatter));
        avaliacao.setSetor(dto.getSetor());
        avaliacao.setLocal(dto.getLocal());
        avaliacao.setAvaliacao(dto.getAvaliacao());
        // 'deleted' será 0 por padrão e 'dataRegistro' será definida pelo @CreationTimestamp
        return avaliacaoRepository.save(avaliacao);
    }

    // --- Listar avaliações (com filtro de status) ---
    public List<Avaliacao> listar(String status) {
        // Retorna avaliações ativas ou deletadas com base no parâmetro 'status'
        if ("deletado".equalsIgnoreCase(status)) { //
            return avaliacaoRepository.findAllByDeletedIsOne(); //
        }
        return avaliacaoRepository.findAllByDeletedIsZero(); //
    }

    // --- Buscar uma avaliação ativa por ID ---
    public Avaliacao buscarPorId(Long id) {
        // Busca a avaliação ativa (deleted = 0) pelo ID ou lança EntityNotFoundException
        return avaliacaoRepository.findByIdAndDeletedIs(id, 0) //
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada ou inativa com o ID: " + id)); //
    }

    // --- Atualizar uma avaliação ---
    @Transactional
    public Avaliacao atualizar(Long id, AtualizarAvaliacaoDto dto) {
        // Busca a avaliação ativa existente
        Avaliacao avaliacao = this.buscarPorId(id); //

        // Atualiza os campos se fornecidos no DTO
        if (dto.getHorario() != null) {
            avaliacao.setHorarioAvaliacao(LocalTime.parse(dto.getHorario(), timeFormatter));
        }
        if (dto.getSetor() != null) {
            avaliacao.setSetor(dto.getSetor());
        }
        if (dto.getLocal() != null) {
            avaliacao.setLocal(dto.getLocal());
        }
        if (dto.getAvaliacao() != null) {
            avaliacao.setAvaliacao(dto.getAvaliacao());
        }

        // Salva as alterações
        return avaliacaoRepository.save(avaliacao); //
    }

    // --- Excluir uma avaliação (Exclusão Lógica) ---
    @Transactional
    public void excluir(Long id) {
        // Busca a avaliação ativa
        Avaliacao avaliacao = this.buscarPorId(id); //
        // Marca como deletada (deleted = 1)
        avaliacao.setDeleted(1); //
        // Salva a alteração no banco
        avaliacaoRepository.save(avaliacao); //
    }
}