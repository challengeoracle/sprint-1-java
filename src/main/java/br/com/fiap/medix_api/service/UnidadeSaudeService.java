package br.com.fiap.medix_api.service;

import br.com.fiap.medix_api.dto.request.AtualizarUnidadeSaudeDto;
import br.com.fiap.medix_api.dto.request.CadastrarUnidadeSaudeDto;
import br.com.fiap.medix_api.model.UnidadeSaude;
import br.com.fiap.medix_api.repository.UnidadeSaudeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class UnidadeSaudeService {

    private UnidadeSaudeRepository unidadeSaudeRepository;

    @Transactional
    public UnidadeSaude criar(CadastrarUnidadeSaudeDto cadastroDto) {
        // Valida se o CNPJ já existe para unidades ativas
        validarNovoCnpj(cadastroDto.getCnpj());

        // Converte o DTO para a entidade e salva
        UnidadeSaude unidade = new UnidadeSaude();
        unidade.setNome(cadastroDto.getNome());
        unidade.setCnpj(cadastroDto.getCnpj());
        unidade.setEndereco(cadastroDto.getEndereco());
        unidade.setTelefone(cadastroDto.getTelefone());
        unidade.setTipoUnidade(cadastroDto.getTipoUnidade());
        return unidadeSaudeRepository.save(unidade);
    }

    public List<UnidadeSaude> listar(String status) {
        // Retorna unidades ativas ou deletadas com base no parâmetro
        if ("deletado".equalsIgnoreCase(status)) {
            return unidadeSaudeRepository.findAllByDeletedIsOne();
        }
        return unidadeSaudeRepository.findAllByDeletedIsZero();
    }

    public UnidadeSaude buscarPorId(Long id) {
        // Busca a unidade de saúde ativa por ID, ou lança uma exceção
        return unidadeSaudeRepository.findByIdAndDeletedIs(id, 0)
                .orElseThrow(() -> new EntityNotFoundException("Unidade de Saúde não encontrada ou inativa com o ID: " + id));
    }

    @Transactional
    public UnidadeSaude atualizar(Long id, AtualizarUnidadeSaudeDto atualizacaoDto) {
        // Busca a unidade, atualiza os campos e salva
        UnidadeSaude unidade = this.buscarPorId(id);

        if (atualizacaoDto.getNome() != null) {
            unidade.setNome(atualizacaoDto.getNome());
        }
        if (atualizacaoDto.getEndereco() != null) {
            unidade.setEndereco(atualizacaoDto.getEndereco());
        }
        if (atualizacaoDto.getTelefone() != null) {
            unidade.setTelefone(atualizacaoDto.getTelefone());
        }
        if (atualizacaoDto.getTipoUnidade() != null) {
            unidade.setTipoUnidade(atualizacaoDto.getTipoUnidade());
        }

        return unidadeSaudeRepository.save(unidade);
    }

    @Transactional
    public void excluir(Long id) {
        // Realiza a exclusão lógica
        UnidadeSaude unidade = this.buscarPorId(id);
        unidade.setDeleted(1);
        unidadeSaudeRepository.save(unidade);
    }

    private void validarNovoCnpj(String cnpj) {
        // Garante que o CNPJ não exista para unidades ativas
        if (unidadeSaudeRepository.findByCnpjAndDeletedIs(cnpj).isPresent()) {
            throw new DataIntegrityViolationException("Já existe uma unidade de saúde com este CNPJ.");
        }
    }
}