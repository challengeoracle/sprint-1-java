package br.com.fiap.medix_api.service;

import br.com.fiap.medix_api.dto.AtualizarPacienteDto;
import br.com.fiap.medix_api.dto.CadastrarPacienteDto;
import br.com.fiap.medix_api.model.Paciente;
import br.com.fiap.medix_api.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Transactional // A transação garante que o save será confirmado (commit) no banco
    public Paciente criar(CadastrarPacienteDto cadastroDto) {
        Paciente paciente = Paciente.builder()
                .nome(cadastroDto.getNome())
                .email(cadastroDto.getEmail())
                .senha(cadastroDto.getSenha())
                .cpf(cadastroDto.getCpf())
                .tipoUsuario("PACIENTE")
                .dataNascimento(cadastroDto.getDataNascimento())
                .numeroConvenio(cadastroDto.getNumeroConvenio())
                .build();
        return pacienteRepository.save(paciente);
    }

    public List<Paciente> listar(String status) {
        if ("deletados".equalsIgnoreCase(status)) {
            // Se o parâmetro for "deletados", busca os deletados (deleted = 1)
            // http://localhost:8080/api/pacientes?status=deletados
            return pacienteRepository.findAllByDeletedIs(1);
        }
        // Por padrão, ou se o parâmetro for qualquer outra coisa, busca os ativos (deleted = 0)
        return pacienteRepository.findAllByDeletedIs(0);
    }

    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findByIdAndDeletedIs(id, 0)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado ou inativo com o ID: " + id));
    }

    @Transactional
    public Paciente atualizar(Long id, AtualizarPacienteDto atualizacaoDto) {

        // Verificando se o paciente existe
        Paciente paciente = this.buscarPorId(id);

        if (atualizacaoDto.getNome() != null) {
            paciente.setNome(atualizacaoDto.getNome());
        }
        if (atualizacaoDto.getEmail() != null) {
            paciente.setEmail(atualizacaoDto.getEmail());
        }
        if (atualizacaoDto.getDataNascimento() != null) {
            paciente.setDataNascimento(atualizacaoDto.getDataNascimento());
        }
        if (atualizacaoDto.getNumeroConvenio() != null) {
            paciente.setNumeroConvenio(atualizacaoDto.getNumeroConvenio());
        }

        return pacienteRepository.save(paciente);
    }

    @Transactional
    public void excluir(Long id) {
        Paciente paciente = this.buscarPorId(id);
        paciente.setDeleted(1);
        pacienteRepository.save(paciente);
    }
}