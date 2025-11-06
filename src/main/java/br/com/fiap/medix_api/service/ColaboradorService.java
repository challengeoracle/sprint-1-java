package br.com.fiap.medix_api.service;

import br.com.fiap.medix_api.dto.request.AtualizarColaboradorDto;
import br.com.fiap.medix_api.dto.request.CadastrarColaboradorDto;
import br.com.fiap.medix_api.model.Colaborador;
import br.com.fiap.medix_api.model.Especialidade; // IMPORTADO
import br.com.fiap.medix_api.model.UnidadeSaude;
import br.com.fiap.medix_api.enums.UsuarioRole;
import br.com.fiap.medix_api.repository.ColaboradorRepository;
import br.com.fiap.medix_api.repository.EspecialidadeRepository; // IMPORTADO
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
    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private UnidadeSaudeService unidadeSaudeService; // INJETADO
    private EspecialidadeRepository especialidadeRepository; // INJETADO

    @Transactional
    public Colaborador criar(CadastrarColaboradorDto cadastroDto) {
        validarNovoColaborador(cadastroDto);

        // ATUALIZADO: Reutiliza o service que já valida se a unidade está ativa
        UnidadeSaude unidade = unidadeSaudeService.buscarPorId(cadastroDto.getIdUnidadeSaude());

        // ATUALIZADO: Busca a especialidade, se ela for informada
        Especialidade especialidade = null;
        if (cadastroDto.getIdEspecialidade() != null) {
            especialidade = especialidadeRepository.findById(cadastroDto.getIdEspecialidade())
                    .orElseThrow(() -> new EntityNotFoundException("Especialidade não encontrada!"));
        }

        Colaborador colaborador = Colaborador.builder()
                .nome(cadastroDto.getNome())
                .email(cadastroDto.getEmail())
                .senha(passwordEncoder.encode(cadastroDto.getSenha()))
                .cpf(cadastroDto.getCpf())
                .tipoUsuario(UsuarioRole.COLABORADOR)
                .unidadeSaude(unidade)
                .especialidade(especialidade) // ATUALIZADO
                .descricaoCargo(cadastroDto.getDescricaoCargo())
                .numeroRegistroProfissional(cadastroDto.getNumeroRegistroProfissional())
                .dataAdmissao(cadastroDto.getDataAdmissao())
                .build();

        return colaboradorRepository.save(colaborador);
    }

    public List<Colaborador> listar(String status) {
        if ("deletado".equalsIgnoreCase(status)) {
            return colaboradorRepository.findAllByDeletedIsOne();
        }
        return colaboradorRepository.findAllByDeletedIsZero();
    }

    public Colaborador buscarPorId(Long id) {
        return colaboradorRepository.findByIdAndDeletedIs(id, 0)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado ou inativo com o ID: " + id));
    }

    @Transactional
    public Colaborador atualizar(Long id, AtualizarColaboradorDto atualizacaoDto) {
        Colaborador colaborador = this.buscarPorId(id);

        if (atualizacaoDto.getNome() != null) {
            colaborador.setNome(atualizacaoDto.getNome());
        }
        if (atualizacaoDto.getEmail() != null) {
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
            if (atualizacaoDto.getDataAdmissao().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("A data de admissão não pode ser no futuro.");
            }
            colaborador.setDataAdmissao(atualizacaoDto.getDataAdmissao());
        }

        // ATUALIZADO: Lógica para atualizar especialidade (opcional)
        if (atualizacaoDto.getIdEspecialidade() != null) {
            Especialidade especialidade = especialidadeRepository.findById(atualizacaoDto.getIdEspecialidade())
                    .orElseThrow(() -> new EntityNotFoundException("Especialidade não encontrada!"));
            colaborador.setEspecialidade(especialidade);
        }

        return colaboradorRepository.save(colaborador);
    }

    @Transactional
    public void excluir(Long id) {
        Colaborador colaborador = this.buscarPorId(id);
        colaborador.setDeleted(1);
        colaboradorRepository.save(colaborador);
    }

    private void validarNovoColaborador(CadastrarColaboradorDto cadastroDto) {
        if (usuarioRepository.findByCpfAndDeletedIs(cadastroDto.getCpf()).isPresent()) {
            throw new DataIntegrityViolationException("Já existe um usuário com este CPF.");
        }
        if (usuarioRepository.findByEmailAndDeletedIs(cadastroDto.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException("Já existe um usuário com este e-mail.");
        }
    }
}