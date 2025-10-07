package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.request.AtualizarColaboradorDto;
import br.com.fiap.medix_api.dto.request.CadastrarColaboradorDto;
import br.com.fiap.medix_api.dto.request.CadastrarPacienteDto;
import br.com.fiap.medix_api.dto.response.RespostaColaboradorDto;
import br.com.fiap.medix_api.dto.response.RespostaPacienteDto;
import br.com.fiap.medix_api.model.Colaborador;
import br.com.fiap.medix_api.model.Paciente;
import br.com.fiap.medix_api.service.ColaboradorService;
import br.com.fiap.medix_api.service.PacienteService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/colaboradores")
@AllArgsConstructor
public class ColaboradorController {

    private final ColaboradorService colaboradorService;
    private final PacienteService pacienteService;

    // Cria um novo colaborador
    @PostMapping
    public ResponseEntity<RespostaColaboradorDto> criar(@RequestBody @Valid CadastrarColaboradorDto colaboradorDto, UriComponentsBuilder uriBuilder) {
        Colaborador colaborador = colaboradorService.criar(colaboradorDto);
        URI uri = uriBuilder.path("/colaboradores/{id}").buildAndExpand(colaborador.getId()).toUri();
        RespostaColaboradorDto responseDto = mapToRespostaColaboradorDto(colaborador);
        return ResponseEntity.created(uri).body(responseDto);
    }

    // Lista todos os colaboradores, com opção de filtrar por status
    @GetMapping
    public ResponseEntity<List<RespostaColaboradorDto>> listar(@RequestParam(required = false) String status) {
        List<Colaborador> colaboradores = colaboradorService.listar(status);
        List<RespostaColaboradorDto> responseDtos = colaboradores.stream()
                .map(this::mapToRespostaColaboradorDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    // Busca um colaborador por ID
    @GetMapping("/{id}")
    public ResponseEntity<RespostaColaboradorDto> buscar(@PathVariable Long id) {
        Colaborador colaborador = colaboradorService.buscarPorId(id);
        RespostaColaboradorDto responseDto = mapToRespostaColaboradorDto(colaborador);
        return ResponseEntity.ok(responseDto);
    }

    // Atualiza os dados de um colaborador
    @PutMapping("/{id}")
    public ResponseEntity<RespostaColaboradorDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarColaboradorDto colaboradorDto) {
        Colaborador colaborador = colaboradorService.atualizar(id, colaboradorDto);
        RespostaColaboradorDto responseDto = mapToRespostaColaboradorDto(colaborador);
        return ResponseEntity.ok(responseDto);
    }

    // Exclui um colaborador de forma lógica
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        colaboradorService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // Cria um paciente por um colaborador
    @PostMapping("/{idColaborador}/pacientes")
    @Transactional
    public ResponseEntity<RespostaPacienteDto> registrarPaciente(
            @PathVariable Long idColaborador,
            @RequestBody @Valid CadastrarPacienteDto pacienteDto,
            UriComponentsBuilder uriBuilder) {

        Paciente paciente = pacienteService.criarPorColaborador(idColaborador, pacienteDto);
        URI uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();

        RespostaPacienteDto responseDto = mapToRespostaPacienteDto(paciente);

        return ResponseEntity.created(uri).body(responseDto);
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

    // Mapeia a entidade Colaborador para o DTO de resposta
    private RespostaColaboradorDto mapToRespostaColaboradorDto(Colaborador colaborador) {
        RespostaColaboradorDto dto = new RespostaColaboradorDto();
        dto.setId(colaborador.getId());
        dto.setNome(colaborador.getNome());
        dto.setEmail(colaborador.getEmail());
        dto.setCpf(colaborador.getCpf());
        dto.setTipoUsuario(colaborador.getTipoUsuario());
        dto.setUnidadeSaude(colaborador.getUnidadeSaude());
        dto.setDescricaoCargo(colaborador.getDescricaoCargo());
        dto.setNumeroRegistroProfissional(colaborador.getNumeroRegistroProfissional());
        dto.setDataAdmissao(colaborador.getDataAdmissao());
        return dto;
    }
}