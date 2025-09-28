// service/PacienteService.java
package br.com.fiap.medix_api.service;

import br.com.fiap.medix_api.dto.AtualizacaoPacienteDto;
import br.com.fiap.medix_api.dto.CadastroPacienteDto;
import br.com.fiap.medix_api.model.Paciente;
import br.com.fiap.medix_api.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class PacienteService {

    private PacienteRepository pacienteRepository;

    // CREATE
    public Paciente criar(CadastroPacienteDto cadastroDto) {
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

    // READ (Listar Todos Ativos)
    public List<Paciente> listarTodos() {
        return pacienteRepository.findAllByDeletedIs(0);
    }

    // READ (Buscar por ID Ativo)
    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findByIdAndDeletedIs(id, 0)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado ou inativo com o ID: " + id));
    }

    // UPDATE
    public Paciente atualizar(Long id, AtualizacaoPacienteDto atualizacaoDto) {
        // Reutiliza a busca para garantir que o paciente exista e está ativo
        Paciente paciente = this.buscarPorId(id);

        // Atualiza os campos do usuário herdado
        paciente.setNome(atualizacaoDto.getNome());
        paciente.setEmail(atualizacaoDto.getEmail());

        // Atualiza os campos específicos do paciente
        paciente.setDataNascimento(atualizacaoDto.getDataNascimento());
        paciente.setNumeroConvenio(atualizacaoDto.getNumeroConvenio());

        // Salva as alterações
        return pacienteRepository.save(paciente);
    }

    // DELETE (Lógico)
    public void excluir(Long id) {
        Paciente paciente = this.buscarPorId(id);
        paciente.setDeleted(1); // Seta a flag para 1 (deletado)
        pacienteRepository.save(paciente);
    }
}