package br.com.fiap.medix_api.service;

import br.com.fiap.medix_api.dto.request.AtualizarColaboradorDto;
import br.com.fiap.medix_api.dto.request.CadastrarColaboradorDto;
import br.com.fiap.medix_api.model.Colaborador;
import br.com.fiap.medix_api.model.UnidadeSaude;
import br.com.fiap.medix_api.enums.UsuarioRole;
import br.com.fiap.medix_api.repository.ColaboradorRepository;
import br.com.fiap.medix_api.repository.UnidadeSaudeRepository;
import br.com.fiap.medix_api.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ColaboradorService {

    private ColaboradorRepository colaboradorRepository;
    private UnidadeSaudeRepository unidadeSaudeRepository;
    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Colaborador criar(CadastrarColaboradorDto cadastroDto) {
        // Valida se o CPF e e-mail do novo colaborador já existem
        validarNovoColaborador(cadastroDto);

        // Busca a unidade de saúde, ou lança uma exceção
        UnidadeSaude unidade = unidadeSaudeRepository.findById(cadastroDto.getIdUnidadeSaude())
                .orElseThrow(() -> new EntityNotFoundException("Unidade de Saúde não encontrada!"));

        // Converte o DTO para a entidade e salva no banco
        Colaborador colaborador = Colaborador.builder()
                .nome(cadastroDto.getNome())
                .email(cadastroDto.getEmail())
                .senha(passwordEncoder.encode(cadastroDto.getSenha()))
                .cpf(cadastroDto.getCpf())
                .tipoUsuario(UsuarioRole.COLABORADOR)
                .unidadeSaude(unidade)
                .descricaoCargo(cadastroDto.getDescricaoCargo())
                .numeroRegistroProfissional(cadastroDto.getNumeroRegistroProfissional())
                .dataAdmissao(cadastroDto.getDataAdmissao())
                .build();

        return colaboradorRepository.save(colaborador);
    }

    public List<Colaborador> listar(String status) {
        // Retorna colaboradores ativos ou deletados com base no parâmetro
        if ("deletado".equalsIgnoreCase(status)) {
            return colaboradorRepository.findAllByDeletedIsOne();
        }
        return colaboradorRepository.findAllByDeletedIsZero();
    }

    public Colaborador buscarPorId(Long id) {
        // Busca um colaborador ativo por ID, ou lança uma exceção
        return colaboradorRepository.findByIdAndDeletedIs(id, 0)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado ou inativo com o ID: " + id));
    }

    @Transactional
    public Colaborador atualizar(Long id, AtualizarColaboradorDto atualizacaoDto) {
        // Busca o colaborador, atualiza os campos e salva
        Colaborador colaborador = this.buscarPorId(id);

        if (atualizacaoDto.getNome() != null) {
            colaborador.setNome(atualizacaoDto.getNome());
        }
        if (atualizacaoDto.getEmail() != null) {
            // Verifica se o novo e-mail já existe
            if (usuarioRepository.findByEmailAndDeletedIs(atualizacaoDto.getEmail()).isPresent()) {
                throw new DataIntegrityViolationException("Já existe um usuário com este e-mail.");
            }
            colaborador.setEmail(atualizacaoDto.getEmail());
        }
        if (atualizacaoDto.getDescricaoCargo() != null) {
            colaborador.setDescricaoCargo(atualizacaoDto.getDescricaoCargo());
        }
        if (atualizacaoDto.getNumeroRegistroProfissional() != null) {
            colaborador.setNumeroRegistroProfissional(atualizacaoDto.getNumeroRegistroProfissional());
        }
        if (atualizacaoDto.getDataAdmissao() != null) {
            // Valida a data de admissão
            if (atualizacaoDto.getDataAdmissao().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("A data de admissão não pode ser no futuro.");
            }
            colaborador.setDataAdmissao(atualizacaoDto.getDataAdmissao());
        }

        return colaboradorRepository.save(colaborador);
    }

    @Transactional
    public void excluir(Long id) {
        // Realiza a exclusão lógica
        Colaborador colaborador = this.buscarPorId(id);
        colaborador.setDeleted(1);
        colaboradorRepository.save(colaborador);
    }

    private void validarNovoColaborador(CadastrarColaboradorDto cadastroDto) {
        // Garante que o CPF e e-mail não existam para usuários ativos
        if (usuarioRepository.findByCpfAndDeletedIs(cadastroDto.getCpf()).isPresent()) {
            throw new DataIntegrityViolationException("Já existe um usuário com este CPF.");
        }
        if (usuarioRepository.findByEmailAndDeletedIs(cadastroDto.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException("Já existe um usuário com este e-mail.");
        }
    }
}