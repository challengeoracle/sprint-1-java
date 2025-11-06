package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.request.AtualizarPacienteDto;
import br.com.fiap.medix_api.dto.response.RespostaPacienteDto;
import br.com.fiap.medix_api.model.Paciente;
import br.com.fiap.medix_api.service.ModelMapper;
import br.com.fiap.medix_api.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/pacientes")
@AllArgsConstructor
@Tag(
        name = "Pacientes",
        description = "Gerencia o cadastro, atualização, listagem e exclusão lógica de pacientes no sistema."
)
public class PacienteController {

    private final PacienteService pacienteService;
    private final ModelMapper modelMapper;

    // Listar pacientes
    @Operation(
            summary = "Listar pacientes",
            description = "Retorna todos os pacientes ativos. Use `?status=deletado` para listar inativos.",
            responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = RespostaPacienteDto.class)))
    )
    @GetMapping
    public ResponseEntity<List<RespostaPacienteDto>> listar(@RequestParam(required = false) String status) {
        List<Paciente> pacientes = pacienteService.listar(status);
        List<RespostaPacienteDto> responseDtos = pacientes.stream()
                .map(modelMapper::mapPacienteToDto)
                .collect(Collectors.toList());

        responseDtos.forEach(dto -> dto.add(linkTo(methodOn(PacienteController.class).buscar(dto.getId())).withSelfRel()));

        return ResponseEntity.ok(responseDtos);
    }

    // Buscar paciente por ID
    @Operation(
            summary = "Buscar paciente por ID",
            description = "Retorna os dados de um paciente específico.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paciente encontrado.", content = @Content(schema = @Schema(implementation = RespostaPacienteDto.class))),
                    @ApiResponse(responseCode = "404", description = "Paciente não encontrado.")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RespostaPacienteDto> buscar(@PathVariable Long id) {
        Paciente paciente = pacienteService.buscarPorId(id);
        RespostaPacienteDto responseDto = modelMapper.mapPacienteToDto(paciente);

        responseDto.add(linkTo(methodOn(PacienteController.class).buscar(id)).withSelfRel());
        responseDto.add(linkTo(methodOn(PacienteController.class).atualizar(id, null)).withRel("atualizar"));
        responseDto.add(linkTo(methodOn(PacienteController.class).excluir(id)).withRel("excluir"));
        responseDto.add(linkTo(methodOn(PacienteController.class).listar(null)).withRel("todos"));

        return ResponseEntity.ok(responseDto);
    }

    // Atualizar paciente
    @Operation(
            summary = "Atualizar paciente",
            description = "Atualiza as informações de um paciente existente. Apenas os campos enviados são modificados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso.", content = @Content(schema = @Schema(implementation = RespostaPacienteDto.class))),
                    @ApiResponse(responseCode = "404", description = "Paciente não encontrado."),
                    @ApiResponse(responseCode = "409", description = "E-mail ou CPF já existentes.")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<RespostaPacienteDto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarPacienteDto pacienteDto
    ) {
        Paciente paciente = pacienteService.atualizar(id, pacienteDto);
        RespostaPacienteDto responseDto = modelMapper.mapPacienteToDto(paciente);
        responseDto.add(linkTo(methodOn(PacienteController.class).buscar(id)).withSelfRel());
        return ResponseEntity.ok(responseDto);
    }

    // Excluir paciente (lógico)
    @Operation(
            summary = "Excluir paciente (lógico)",
            description = "Realiza a exclusão lógica, marcando o paciente como inativo.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Paciente excluído com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Paciente não encontrado.")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pacienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}