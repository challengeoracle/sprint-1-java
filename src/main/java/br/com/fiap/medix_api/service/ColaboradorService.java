package br.com.fiap.medix_api.service;

import br.com.fiap.medix_api.dto.AtualizarColaboradorDto;
import br.com.fiap.medix_api.dto.CadastrarColaboradorDto;
import br.com.fiap.medix_api.model.Colaborador;
import br.com.fiap.medix_api.model.UnidadeSaude;
import br.com.fiap.medix_api.repository.ColaboradorRepository;
import br.com.fiap.medix_api.repository.UnidadeSaudeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ColaboradorService {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private UnidadeSaudeRepository unidadeSaudeRepository;

    // CREATE
    @Transactional
    public Colaborador criar(CadastrarColaboradorDto cadastroDto) {
        UnidadeSaude unidade = unidadeSaudeRepository.findById(cadastroDto.getIdUnidadeSaude())
                .orElseThrow(() -> new EntityNotFoundException("Unidade de Saúde não encontrada!"));

        Colaborador colaborador = Colaborador.builder()
                .nome(cadastroDto.getNome())
                .email(cadastroDto.getEmail())
                .senha(cadastroDto.getSenha())
                .cpf(cadastroDto.getCpf())
                .tipoUsuario("COLABORADOR")
                .unidadeSaude(unidade)
                .cargo(cadastroDto.getCargo())
                .build();

        return colaboradorRepository.save(colaborador);
    }

    // READ (Listar com filtro de status)
    public List<Colaborador> listar(String status) {
        if ("deletados".equalsIgnoreCase(status)) {
            return colaboradorRepository.findAllByDeletedIs(1);
        }
        return colaboradorRepository.findAllByDeletedIs(0);
    }

    // READ (Buscar por ID Ativo)
    public Colaborador buscarPorId(Long id) {
        return colaboradorRepository.findByIdAndDeletedIs(id, 0)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado ou inativo com o ID: " + id));
    }

    // UPDATE (Com lógica de atualização parcial)
    @Transactional
    public Colaborador atualizar(Long id, AtualizarColaboradorDto atualizacaoDto) {
        Colaborador colaborador = this.buscarPorId(id);

        if (atualizacaoDto.getNome() != null) {
            colaborador.setNome(atualizacaoDto.getNome());
        }
        if (atualizacaoDto.getEmail() != null) {
            colaborador.setEmail(atualizacaoDto.getEmail());
        }
        if (atualizacaoDto.getCargo() != null) {
            colaborador.setCargo(atualizacaoDto.getCargo());
        }

        return colaboradorRepository.save(colaborador);
    }

    // DELETE (Lógico)
    @Transactional
    public void excluir(Long id) {
        Colaborador colaborador = this.buscarPorId(id);
        colaborador.setDeleted(1);
        colaboradorRepository.save(colaborador);
    }
}