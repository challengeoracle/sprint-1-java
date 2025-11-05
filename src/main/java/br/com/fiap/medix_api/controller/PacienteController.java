package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.request.AtualizarPacienteDto;
import br.com.fiap.medix_api.dto.request.CadastrarPacienteDto;
import br.com.fiap.medix_api.dto.response.RespostaPacienteDto;
import br.com.fiap.medix_api.model.Paciente;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pacientes")
@AllArgsConstructor
@Tag(
        name = "Pacientes",
        description = "Gerencia o cadastro, atualização, listagem e exclusão lógica de pacientes no sistema."
)
public class PacienteController {

    private final PacienteService pacienteService;

    @Operation(
            summary = "Listar pacientes",
            description = """
            Retorna uma lista de pacientes cadastrados no sistema.
            É possível filtrar opcionalmente pelo parâmetro `status` (ex: ativo ou inativo).
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de pacientes retornada com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RespostaPacienteDto.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<RespostaPacienteDto>> listar(@RequestParam(required = false) String status) {
        List<Paciente> pacientes = pacienteService.listar(status);
        List<RespostaPacienteDto> responseDtos = pacientes.stream()
                .map(this::mapToRespostaPacienteDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @Operation(
            summary = "Buscar paciente por ID",
            description = "Retorna os dados de um paciente específico com base em seu ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Paciente encontrado com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RespostaPacienteDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Paciente não encontrado.",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RespostaPacienteDto> buscar(@PathVariable Long id) {
        Paciente paciente = pacienteService.buscarPorId(id);
        RespostaPacienteDto responseDto = mapToRespostaPacienteDto(paciente);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Atualizar dados de um paciente",
            description = """
            Atualiza as informações de um paciente existente com base no seu ID.
            Todos os campos enviados substituirão os valores atuais.
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Paciente atualizado com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RespostaPacienteDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Paciente não encontrado.",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<RespostaPacienteDto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarPacienteDto pacienteDto
    ) {
        Paciente paciente = pacienteService.atualizar(id, pacienteDto);
        RespostaPacienteDto responseDto = mapToRespostaPacienteDto(paciente);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "Excluir paciente (lógico)",
            description = """
            Realiza a exclusão lógica de um paciente, mantendo o registro no banco de dados,
            mas marcando-o como inativo.
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Paciente excluído com sucesso."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Paciente não encontrado.",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pacienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // --- Método auxiliar para conversão de entidade para DTO ---
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
