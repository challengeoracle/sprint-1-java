package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.request.AtualizarPacienteDto;
import br.com.fiap.medix_api.dto.request.CadastrarPacienteDto;
import br.com.fiap.medix_api.dto.response.RespostaPacienteDto;
import br.com.fiap.medix_api.model.Paciente;
import br.com.fiap.medix_api.service.PacienteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pacientes")
@AllArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    // Lista todos os pacientes, com opção de filtrar por status
    @GetMapping
    public ResponseEntity<List<RespostaPacienteDto>> listar(@RequestParam(required = false) String status) {
        List<Paciente> pacientes = pacienteService.listar(status);
        List<RespostaPacienteDto> responseDtos = pacientes.stream()
                .map(this::mapToRespostaPacienteDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    // Busca um paciente por ID
    @GetMapping("/{id}")
    public ResponseEntity<RespostaPacienteDto> buscar(@PathVariable Long id) {
        Paciente paciente = pacienteService.buscarPorId(id);
        RespostaPacienteDto responseDto = mapToRespostaPacienteDto(paciente);
        return ResponseEntity.ok(responseDto);
    }

    // Atualiza os dados de um paciente
    @PutMapping("/{id}")
    public ResponseEntity<RespostaPacienteDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarPacienteDto pacienteDto) {
        Paciente paciente = pacienteService.atualizar(id, pacienteDto);
        RespostaPacienteDto responseDto = mapToRespostaPacienteDto(paciente);
        return ResponseEntity.ok(responseDto);
    }

    // Exclui um paciente de forma lógica
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pacienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // Mapeia a entidade Paciente para o DTO de resposta
    private RespostaPacienteDto mapToRespostaPacienteDto(Paciente paciente) {
        RespostaPacienteDto dto = new RespostaPacienteDto();
        dto.setId(paciente.getId());
        dto.setNome(paciente.getNome());
        dto.setEmail(paciente.getEmail());
        dto.setCpf(paciente.getCpf());
        dto.setTipoUsuario(paciente.getTipoUsuario());
        dto.setDataNascimento(paciente.getDataNascimento());
        dto.setTipoSanguineo(paciente.getTipoSanguineo());
        dto.setGenero(paciente.getGenero());
        dto.setAlergias(paciente.getAlergias());
        return dto;
    }
}