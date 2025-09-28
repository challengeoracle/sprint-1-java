// service/ColaboradorService.java
package br.com.fiap.medix_api.service;

import br.com.fiap.medix_api.dto.AtualizacaoColaboradorDto;
import br.com.fiap.medix_api.dto.CadastroColaboradorDto;
import br.com.fiap.medix_api.model.Colaborador;
import br.com.fiap.medix_api.model.UnidadeSaude;
import br.com.fiap.medix_api.repository.ColaboradorRepository;
import br.com.fiap.medix_api.repository.UnidadeSaudeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ColaboradorService {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private UnidadeSaudeRepository unidadeSaudeRepository;

    // CREATE
    public Colaborador criar(CadastroColaboradorDto cadastroDto) {
        UnidadeSaude unidade = unidadeSaudeRepository.findById(cadastroDto.getIdUnidadeSaude())
                .orElseThrow(() -> new EntityNotFoundException("Unidade de Saúde não encontrada!"));

        // Conversão para a entidade
        Colaborador colaborador = Colaborador.builder()
                .nome(cadastroDto.getNome())
                .email(cadastroDto.getEmail())
                .senha(cadastroDto.getSenha())
                .cpf(cadastroDto.getCpf())
                .tipoUsuario("COLABORADOR")
                .unidadeSaude(unidade)
                .cargo(cadastroDto.getCargo())
                .build();

        return colaboradorRepository.save(colaborador); // CORRIGIDO: movido para dentro do método
    }

    // READ (Listar Todos Ativos)
    public List<Colaborador> listarTodos() {
        return colaboradorRepository.findAllByDeletedIs(0);
    }

    // READ (Buscar por ID Ativo)
    public Colaborador buscarPorId(Long id) {
        return colaboradorRepository.findByIdAndDeletedIs(id, 0)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado ou inativo com o ID: " + id));
    }

    // UPDATE
    public Colaborador atualizar(Long id, AtualizacaoColaboradorDto atualizacaoDto) {
        Colaborador colaborador = this.buscarPorId(id);

        // Atualiza os campos do usuário herdado
        colaborador.setNome(atualizacaoDto.getNome());
        colaborador.setEmail(atualizacaoDto.getEmail());

        // Atualiza os campos específicos do colaborador
        colaborador.setCargo(atualizacaoDto.getCargo());

        return colaboradorRepository.save(colaborador);
    }

    // DELETE (Lógico)
    public void excluir(Long id) {
        Colaborador colaborador = this.buscarPorId(id);
        colaborador.setDeleted(1);
        colaboradorRepository.save(colaborador);
    }
}