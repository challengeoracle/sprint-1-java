package br.com.fiap.medix_api.service;

import br.com.fiap.medix_api.dto.request.AtualizarPacienteDto;
import br.com.fiap.medix_api.dto.request.CadastrarPacienteDto;
import br.com.fiap.medix_api.model.Paciente;
import br.com.fiap.medix_api.enums.UsuarioRole;
import br.com.fiap.medix_api.repository.PacienteRepository;
import br.com.fiap.medix_api.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PacienteService {

    private PacienteRepository pacienteRepository;
    private UsuarioRepository usuarioRepository;

    private PasswordEncoder passwordEncoder;

    @Transactional
    public Paciente criarPorColaborador(Long idColaborador, CadastrarPacienteDto cadastroDto) {
        // Valida se o colaborador que faz o cadastro existe
        usuarioRepository.findByIdAndDeletedIs(idColaborador, 0)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador com ID " + idColaborador + " não encontrado ou inativo."));

        // Valida se os dados do novo paciente já existem
        validarNovoPaciente(cadastroDto);

        // Converte o DTO para a entidade e salva no banco
        Paciente paciente = construirPaciente(cadastroDto);

        return pacienteRepository.save(paciente);
    }

    public List<Paciente> listar(String status) {
        // Retorna pacientes ativos ou deletados com base no parâmetro
        if ("deletado".equalsIgnoreCase(status)) {
            return pacienteRepository.findAllByDeletedIsOne();
        }
        return pacienteRepository.findAllByDeletedIsZero();
    }

    public Paciente buscarPorId(Long id) {
        // Busca um paciente ativo por ID, ou lança uma exceção
        return pacienteRepository.findByIdAndDeletedIs(id, 0)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado ou inativo com o ID: " + id));
    }

    @Transactional
    public Paciente atualizar(Long id, AtualizarPacienteDto atualizacaoDto) {
        // Busca o paciente, atualiza os campos e salva
        Paciente paciente = this.buscarPorId(id);

        if (atualizacaoDto.getNome() != null) {
            paciente.setNome(atualizacaoDto.getNome());
        }
        if (atualizacaoDto.getEmail() != null) {
            // Verifica se o e-mail já existe antes de atualizar
            if (usuarioRepository.findByEmailAndDeletedIs(atualizacaoDto.getEmail()).isPresent()) {
                throw new DataIntegrityViolationException("Já existe um usuário com este e-mail.");
            }
            paciente.setEmail(atualizacaoDto.getEmail());
        }
        if (atualizacaoDto.getDataNascimento() != null) {
            paciente.setDataNascimento(atualizacaoDto.getDataNascimento());
        }
        if (atualizacaoDto.getTipoSanguineo() != null) {
            paciente.setTipoSanguineo(atualizacaoDto.getTipoSanguineo());
        }
        if (atualizacaoDto.getGenero() != null) {
            paciente.setGenero(atualizacaoDto.getGenero());
        }
        if (atualizacaoDto.getAlergias() != null) {
            paciente.setAlergias(atualizacaoDto.getAlergias());
        }

        return pacienteRepository.save(paciente);
    }

    @Transactional
    public void excluir(Long id) {
        // Realiza a exclusão lógica, marcando o registro como deletado
        Paciente paciente = this.buscarPorId(id);
        paciente.setDeleted(1);
        pacienteRepository.save(paciente);
    }

    private void validarNovoPaciente(CadastrarPacienteDto cadastroDto) {
        // Garante que o CPF e o e-mail não existam para usuários ativos
        if (usuarioRepository.findByCpfAndDeletedIs(cadastroDto.getCpf()).isPresent()) {
            throw new DataIntegrityViolationException("Já existe um usuário com este CPF.");
        }
        if (usuarioRepository.findByEmailAndDeletedIs(cadastroDto.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException("Já existe um usuário com este e-mail.");
        }
    }

    private Paciente construirPaciente(CadastrarPacienteDto cadastroDto) {
        // Constrói a entidade Paciente a partir do DTO de cadastro
        return Paciente.builder()
                .nome(cadastroDto.getNome())
                .email(cadastroDto.getEmail())
                .senha(passwordEncoder.encode(cadastroDto.getSenha()))
                .cpf(cadastroDto.getCpf())
                .tipoUsuario(UsuarioRole.PACIENTE)
                .dataNascimento(cadastroDto.getDataNascimento())
                .tipoSanguineo(cadastroDto.getTipoSanguineo())
                .genero(cadastroDto.getGenero())
                .alergias(cadastroDto.getAlergias())
                .build();
    }
}