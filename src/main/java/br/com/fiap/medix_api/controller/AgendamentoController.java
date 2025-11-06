package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.request.CadastrarAgendamentoDto;
import br.com.fiap.medix_api.dto.response.RespostaAgendamentoDto;
import br.com.fiap.medix_api.dto.response.RespostaColaboradorDto;
import br.com.fiap.medix_api.enums.StatusAgendamento;
import br.com.fiap.medix_api.model.Agendamento;
import br.com.fiap.medix_api.model.Colaborador;
import br.com.fiap.medix_api.model.Especialidade;
import br.com.fiap.medix_api.model.UnidadeSaude;
import br.com.fiap.medix_api.service.AgendamentoService;
import br.com.fiap.medix_api.service.DisponibilidadeService;
import br.com.fiap.medix_api.service.ModelMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
@Tag(name = "Agendamentos", description = "Central de agendamentos: disponibilidade, criação, listagem e cancelamento.")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final DisponibilidadeService disponibilidadeService;
    private final ModelMapper modelMapper;

    // Rotas de consulta de disponibilidade (5 passos)

    @GetMapping("/disponibilidade/especialidades")
    @Operation(
            summary = "Passo 1: Listar especialidades",
            description = "Retorna todas as especialidades que possuem colaboradores ativos.",
            responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Especialidade.class)))
    )
    public ResponseEntity<List<Especialidade>> listarEspecialidades() {
        List<Especialidade> especialidades = disponibilidadeService.listarEspecialidades();
        especialidades.forEach(e -> {
            e.add(linkTo(methodOn(AgendamentoController.class).listarUnidades(e.getId())).withRel("unidades"));
        });
        return ResponseEntity.ok(especialidades);
    }

    @GetMapping("/disponibilidade/unidades")
    @Operation(
            summary = "Passo 2: Listar unidades por especialidade",
            description = "Retorna as unidades de saúde onde a especialidade informada é atendida.",
            responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UnidadeSaude.class)))
    )
    public ResponseEntity<List<UnidadeSaude>> listarUnidades(@RequestParam Long especialidadeId) {
        List<UnidadeSaude> unidades = disponibilidadeService.listarUnidades(especialidadeId);
        unidades.forEach(u -> {
            u.add(linkTo(methodOn(UnidadeSaudeController.class).buscar(u.getId())).withSelfRel());
            u.add(linkTo(methodOn(AgendamentoController.class).listarDias(u.getId(), especialidadeId)).withRel("dias_disponiveis"));
        });
        return ResponseEntity.ok(unidades);
    }

    @GetMapping("/disponibilidade/dias")
    @Operation(
            summary = "Passo 3: Listar dias disponíveis",
            description = "Retorna uma lista de datas futuras com base na unidade e especialidade, excluindo fins de semana (exemplo).",
            responses = @ApiResponse(responseCode = "200")
    )
    public ResponseEntity<List<LocalDate>> listarDias(@RequestParam Long unidadeId, @RequestParam Long especialidadeId) {
        return ResponseEntity.ok(disponibilidadeService.listarDiasDisponiveis(unidadeId, especialidadeId));
    }

    @GetMapping("/disponibilidade/horarios")
    @Operation(
            summary = "Passo 4: Listar horários disponíveis no dia",
            description = "Retorna uma lista de horários de 30 em 30 minutos onde pelo menos um colaborador está livre.",
            responses = @ApiResponse(responseCode = "200")
    )
    public ResponseEntity<List<LocalTime>> listarHorarios(
            @RequestParam Long unidadeId,
            @RequestParam Long especialidadeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(disponibilidadeService.listarHorariosDisponiveis(unidadeId, especialidadeId, data));
    }

    @GetMapping("/disponibilidade/profissionais")
    @Operation(
            summary = "Passo 5: Listar profissionais disponíveis no horário",
            description = "Retorna a lista exata de colaboradores que estão livres no horário e local especificados.",
            responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = RespostaColaboradorDto.class)))
    )
    public ResponseEntity<List<RespostaColaboradorDto>> listarProfissionais(
            @RequestParam Long unidadeId,
            @RequestParam Long especialidadeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horario) {

        LocalDateTime dataHora = LocalDateTime.of(data, horario);
        List<Colaborador> disponiveis = disponibilidadeService.listarProfissionaisDisponiveis(unidadeId, especialidadeId, dataHora);
        List<RespostaColaboradorDto> dtos = disponiveis.stream().map(modelMapper::mapColaboradorToDto).toList();
        dtos.forEach(colab -> colab.add(linkTo(methodOn(ColaboradorController.class).buscar(colab.getId())).withSelfRel()));
        return ResponseEntity.ok(dtos);
    }

    // Rotas de Agendamento (Criação e Leitura)

    @PostMapping
    @Operation(
            summary = "Passo 6: Criar agendamento (Finalizar)",
            description = "Registra um novo agendamento. Realiza validações de conflito (médico, paciente e sala).",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Agendamento criado.", content = @Content(schema = @Schema(implementation = RespostaAgendamentoDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos."),
                    @ApiResponse(responseCode = "409", description = "Conflito de horário/regra de negócio.")
            }
    )
    public ResponseEntity<RespostaAgendamentoDto> agendar(@RequestBody @Valid CadastrarAgendamentoDto dto,
                                                          UriComponentsBuilder uriBuilder) {
        Agendamento agendamento = agendamentoService.agendar(dto);
        URI uri = uriBuilder.path("/agendamentos/{id}").buildAndExpand(agendamento.getId()).toUri();

        RespostaAgendamentoDto dtoResponse = modelMapper.mapAgendamentoToDto(agendamento);
        dtoResponse.add(linkTo(methodOn(AgendamentoController.class).buscarPorId(dtoResponse.getId())).withSelfRel());

        return ResponseEntity.created(uri).body(dtoResponse);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar agendamento por ID",
            description = "Retorna os detalhes de um agendamento específico.",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = RespostaAgendamentoDto.class))),
                    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado.")
            }
    )
    public ResponseEntity<RespostaAgendamentoDto> buscarPorId(@PathVariable Long id) {
        RespostaAgendamentoDto dto = modelMapper.mapAgendamentoToDto(agendamentoService.buscarPorId(id));
        dto.add(linkTo(methodOn(AgendamentoController.class).buscarPorId(id)).withSelfRel());
        dto.add(linkTo(methodOn(PacienteController.class).buscar(dto.getIdPaciente())).withRel("paciente"));
        dto.add(linkTo(methodOn(ColaboradorController.class).buscar(dto.getIdColaborador())).withRel("colaborador"));
        dto.add(linkTo(methodOn(UnidadeSaudeController.class).buscar(dto.getIdUnidadeSaude())).withRel("unidade_saude"));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(
            summary = "Histórico do paciente",
            description = "Lista todos os agendamentos já realizados e futuros de um paciente.",
            responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = RespostaAgendamentoDto.class)))
    )
    public ResponseEntity<List<RespostaAgendamentoDto>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<RespostaAgendamentoDto> dtos = agendamentoService.listarPorPaciente(pacienteId).stream().map(modelMapper::mapAgendamentoToDto).toList();
        dtos.forEach(dto -> dto.add(linkTo(methodOn(AgendamentoController.class).buscarPorId(dto.getId())).withSelfRel()));
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/paciente/{pacienteId}/proxima")
    @Operation(
            summary = "Próxima consulta do paciente",
            description = "Retorna a consulta futura mais próxima com status AGENDADO ou CONFIRMADO. Retorna 404 se não houver nenhuma.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Próxima consulta encontrada.", content = @Content(schema = @Schema(implementation = RespostaAgendamentoDto.class))),
                    @ApiResponse(responseCode = "404", description = "Nenhuma consulta futura/ativa encontrada.")
            }
    )
    public ResponseEntity<RespostaAgendamentoDto> buscarProximaConsulta(@PathVariable Long pacienteId) {
        return agendamentoService.buscarProximaConsultaPaciente(pacienteId)
                .map(agendamento -> {
                    RespostaAgendamentoDto dto = modelMapper.mapAgendamentoToDto(agendamento);
                    dto.add(linkTo(methodOn(AgendamentoController.class).buscarPorId(dto.getId())).withSelfRel());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/colaborador/{colaboradorId}")
    @Operation(
            summary = "Agenda do colaborador",
            description = "Lista todos os agendamentos, passados e futuros, de um colaborador específico.",
            responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = RespostaAgendamentoDto.class)))
    )
    public ResponseEntity<List<RespostaAgendamentoDto>> listarPorColaborador(@PathVariable Long colaboradorId) {
        List<RespostaAgendamentoDto> dtos = agendamentoService.listarPorColaborador(colaboradorId).stream().map(modelMapper::mapAgendamentoToDto).toList();
        dtos.forEach(dto -> dto.add(linkTo(methodOn(AgendamentoController.class).buscarPorId(dto.getId())).withSelfRel()));
        return ResponseEntity.ok(dtos);
    }

    // Operações de modificação de status
    @PatchMapping("/{id}/confirmar")
    @Operation(
            summary = "Confirmar presença",
            description = "Atualiza o status do agendamento para CONFIRMADO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status atualizado.", content = @Content(schema = @Schema(implementation = RespostaAgendamentoDto.class))),
                    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado."),
                    @ApiResponse(responseCode = "409", description = "Não é possível alterar status já cancelado/finalizado.")
            }
    )
    public ResponseEntity<RespostaAgendamentoDto> confirmar(@PathVariable Long id) {
        RespostaAgendamentoDto dto = modelMapper.mapAgendamentoToDto(agendamentoService.atualizarStatus(id, StatusAgendamento.CONFIRMADO));
        dto.add(linkTo(methodOn(AgendamentoController.class).buscarPorId(id)).withSelfRel());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}/cancelar/paciente")
    @Operation(
            summary = "Cancelar pelo paciente",
            description = "Marca o agendamento como CANCELADO_PACIENTE, liberando a agenda para outros.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status CANCELADO_PACIENTE."),
                    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado.")
            }
    )
    public ResponseEntity<RespostaAgendamentoDto> cancelarPeloPaciente(@PathVariable Long id) {
        RespostaAgendamentoDto dto = modelMapper.mapAgendamentoToDto(agendamentoService.cancelarPorPaciente(id));
        dto.add(linkTo(methodOn(AgendamentoController.class).buscarPorId(id)).withSelfRel());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}/cancelar/colaborador")
    @Operation(
            summary = "Cancelar pelo colaborador",
            description = "Marca o agendamento como CANCELADO_COLABORADOR (uso interno), liberando a agenda.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status CANCELADO_COLABORADOR."),
                    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado.")
            }
    )
    public ResponseEntity<RespostaAgendamentoDto> cancelarPeloColaborador(@PathVariable Long id) {
        RespostaAgendamentoDto dto = modelMapper.mapAgendamentoToDto(agendamentoService.cancelarPorColaborador(id));
        dto.add(linkTo(methodOn(AgendamentoController.class).buscarPorId(id)).withSelfRel());
        return ResponseEntity.ok(dto);
    }

}