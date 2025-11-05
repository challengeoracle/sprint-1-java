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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Colaboradores",
        description = "Gerencia o cadastro, atualização, listagem e exclusão lógica dos colaboradores. Também permite o registro de pacientes por um colaborador."
)
public class ColaboradorController {

    private final ColaboradorService colaboradorService;
    private final PacienteService pacienteService;

    @Operation(
            summary = "Criar novo colaborador",
            description = """
            Cadastra um novo colaborador no sistema, vinculando seus dados profissionais e pessoais.
            Retorna o colaborador criado com seu respectivo ID.
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Colaborador criado com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RespostaColaboradorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos enviados na requisição.",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<RespostaColaboradorDto> criar(
            @RequestBody @Valid CadastrarColaboradorDto colaboradorDto,
            UriComponentsBuilder uriBuilder
    ) {
        Colaborador colaborador = colaboradorService.criar(colaboradorDto);
        URI uri = uriBuilder.path("/colaboradores/{id}").buildAndExpand(colaborador.getId()).toUri();
        RespostaColaboradorDto responseDto = mapToRespostaColaboradorDto(colaborador);
        return ResponseEntity.created(uri).body(responseDto);
    }

    @Operation(
            summary = "Listar colaboradores",
            description = """
            Retorna uma lista de colaboradores cadastrados.
            É possível filtrar pelo parâmetro opcional `status` (ex: ativo ou inativo).
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de colaboradores retornada com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RespostaColaboradorDto.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<RespostaColaboradorDto>> listar(@RequestParam(required = false) String status) {
        List<Colaborador> colaboradores = colaboradorService.listar(status);
        List<RespostaColaboradorDto> responseDtos = colaboradores.stream()
                .map(this::mapToRespostaColaboradorDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @Operation(
            summary = "Buscar colaborador por ID",
            description = "Retorna os dados de um colaborador específico com base em seu ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Colaborador encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RespostaColaboradorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Colaborador não encontrado.",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RespostaColaboradorDto> buscar(@PathVariable Long id) {
        Colaborador colaborador = colaboradorService.buscarPorId(id);
        RespostaColaboradorDto responseDto = mapToRespostaColaboradorDto(colaborador);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Atualizar dados de um colaborador",
            description = """
            Atualiza as informações de um colaborador existente.
            Todos os campos enviados substituirão os valores atuais.
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Colaborador atualizado com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RespostaColaboradorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Colaborador não encontrado.",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<RespostaColaboradorDto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarColaboradorDto colaboradorDto
    ) {
        Colaborador colaborador = colaboradorService.atualizar(id, colaboradorDto);
        RespostaColaboradorDto responseDto = mapToRespostaColaboradorDto(colaborador);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Excluir colaborador (lógico)",
            description = """
            Realiza a exclusão lógica de um colaborador, mantendo o registro no banco de dados, mas marcando-o como inativo.
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Colaborador excluído com sucesso."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Colaborador não encontrado.",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        colaboradorService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Registrar paciente por colaborador",
            description = """
            Permite que um colaborador cadastre um novo paciente sob sua responsabilidade.
            Retorna o paciente criado e o respectivo ID.
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Paciente registrado com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RespostaPacienteDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Colaborador não encontrado para o ID informado.",
                            content = @Content
                    )
            }
    )
    @PostMapping("/{idColaborador}/pacientes")
    @Transactional
    public ResponseEntity<RespostaPacienteDto> registrarPaciente(
            @PathVariable Long idColaborador,
            @RequestBody @Valid CadastrarPacienteDto pacienteDto,
            UriComponentsBuilder uriBuilder
    ) {
        Paciente paciente = pacienteService.criarPorColaborador(idColaborador, pacienteDto);
        URI uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        RespostaPacienteDto responseDto = mapToRespostaPacienteDto(paciente);
        return ResponseEntity.created(uri).body(responseDto);
    }

    // --- Métodos auxiliares (mapeamento para DTOs) ---
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
