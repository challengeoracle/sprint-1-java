package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.request.AtualizarColaboradorDto;
import br.com.fiap.medix_api.dto.request.CadastrarColaboradorDto;
import br.com.fiap.medix_api.dto.request.CadastrarPacienteDto;
import br.com.fiap.medix_api.dto.response.RespostaColaboradorDto;
import br.com.fiap.medix_api.dto.response.RespostaPacienteDto;
import br.com.fiap.medix_api.model.Colaborador;
import br.com.fiap.medix_api.model.Paciente;
import br.com.fiap.medix_api.service.ColaboradorService;
import br.com.fiap.medix_api.service.ModelMapper;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    private final ModelMapper modelMapper;

    // Criar novo colaborador
    @Operation(
            summary = "Criar novo colaborador",
            description = "Cadastra um novo colaborador (médico, enfermeiro, etc.) e o vincula a uma unidade/especialidade.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Colaborador criado com sucesso.", content = @Content(schema = @Schema(implementation = RespostaColaboradorDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição."),
                    @ApiResponse(responseCode = "409", description = "CPF ou E-mail já existentes.")
            }
    )
    @PostMapping
    public ResponseEntity<RespostaColaboradorDto> criar(
            @RequestBody @Valid CadastrarColaboradorDto colaboradorDto,
            UriComponentsBuilder uriBuilder
    ) {
        Colaborador colaborador = colaboradorService.criar(colaboradorDto);
        URI uri = uriBuilder.path("/colaboradores/{id}").buildAndExpand(colaborador.getId()).toUri();
        RespostaColaboradorDto responseDto = modelMapper.mapColaboradorToDto(colaborador);
        responseDto.add(linkTo(methodOn(ColaboradorController.class).buscar(colaborador.getId())).withSelfRel());
        return ResponseEntity.created(uri).body(responseDto);
    }

    // Listar colaboradores
    @Operation(
            summary = "Listar colaboradores",
            description = "Retorna uma lista de colaboradores ativos. Use `?status=deletado` para listar inativos.",
            responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = RespostaColaboradorDto.class)))
    )
    @GetMapping
    public ResponseEntity<List<RespostaColaboradorDto>> listar(@RequestParam(required = false) String status) {
        List<Colaborador> colaboradores = colaboradorService.listar(status);
        List<RespostaColaboradorDto> responseDtos = colaboradores.stream()
                .map(modelMapper::mapColaboradorToDto)
                .collect(Collectors.toList());

        responseDtos.forEach(dto -> dto.add(linkTo(methodOn(ColaboradorController.class).buscar(dto.getId())).withSelfRel()));

        return ResponseEntity.ok(responseDtos);
    }

    // Buscar colaborador por ID
    @Operation(
            summary = "Buscar colaborador por ID",
            description = "Retorna os dados de um colaborador específico.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Colaborador encontrado.", content = @Content(schema = @Schema(implementation = RespostaColaboradorDto.class))),
                    @ApiResponse(responseCode = "404", description = "Colaborador não encontrado.")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RespostaColaboradorDto> buscar(@PathVariable Long id) {
        Colaborador colaborador = colaboradorService.buscarPorId(id);
        RespostaColaboradorDto responseDto = modelMapper.mapColaboradorToDto(colaborador);
        responseDto.add(linkTo(methodOn(ColaboradorController.class).buscar(id)).withSelfRel());
        responseDto.add(linkTo(methodOn(ColaboradorController.class).atualizar(id, null)).withRel("atualizar"));
        responseDto.add(linkTo(methodOn(ColaboradorController.class).excluir(id)).withRel("excluir"));
        return ResponseEntity.ok(responseDto);
    }

    // Atualizar dados de um colaborador
    @Operation(
            summary = "Atualizar dados de um colaborador",
            description = "Atualiza as informaçõs de um colaborador existente. Apenas os campos enviados são modificados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Colaborador atualizado com sucesso.", content = @Content(schema = @Schema(implementation = RespostaColaboradorDto.class))),
                    @ApiResponse(responseCode = "404", description = "Colaborador não encontrado."),
                    @ApiResponse(responseCode = "409", description = "E-mail já existente.")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<RespostaColaboradorDto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarColaboradorDto colaboradorDto
    ) {
        Colaborador colaborador = colaboradorService.atualizar(id, colaboradorDto);
        RespostaColaboradorDto responseDto = modelMapper.mapColaboradorToDto(colaborador);
        responseDto.add(linkTo(methodOn(ColaboradorController.class).buscar(id)).withSelfRel());
        return ResponseEntity.ok(responseDto);
    }

    // Excluir colaborador (lógico)
    @Operation(
            summary = "Excluir colaborador (lógico)",
            description = "Realiza a exclusão lógica, marcando o colaborador como inativo.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Colaborador excluído com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Colaborador não encontrado.")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        colaboradorService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // Registrar paciente por colaborador
    @Operation(
            summary = "Registrar paciente por colaborador",
            description = "Permite que um colaborador cadastre um novo paciente no sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Paciente registrado com sucesso.", content = @Content(schema = @Schema(implementation = RespostaPacienteDto.class))),
                    @ApiResponse(responseCode = "404", description = "Colaborador não encontrado para o ID informado.")
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
        RespostaPacienteDto responseDto = modelMapper.mapPacienteToDto(paciente);
        responseDto.add(linkTo(methodOn(PacienteController.class).buscar(paciente.getId())).withSelfRel());
        return ResponseEntity.created(uri).body(responseDto);
    }
}