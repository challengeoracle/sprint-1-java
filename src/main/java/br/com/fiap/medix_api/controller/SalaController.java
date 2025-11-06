package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.request.CadastrarSalaDto;
import br.com.fiap.medix_api.dto.response.RespostaSalaDto;
import br.com.fiap.medix_api.model.Sala;
import br.com.fiap.medix_api.service.ModelMapper;
import br.com.fiap.medix_api.service.SalaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/salas")
@RequiredArgsConstructor
@Tag(name = "Salas", description = "Gerenciamento de salas e consultórios das unidades de saúde.")
public class SalaController {

    private final SalaService salaService;
    private final ModelMapper modelMapper;

    // Cadastrar nova sala
    @PostMapping
    @Operation(
            summary = "Cadastrar nova sala",
            description = "Cria um novo registro de sala ou consultório, vinculando-o a uma unidade de saúde.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sala cadastrada com sucesso.", content = @Content(schema = @Schema(implementation = RespostaSalaDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição."),
                    @ApiResponse(responseCode = "404", description = "Unidade de Saúde não encontrada.")
            }
    )
    public ResponseEntity<RespostaSalaDto> criar(@RequestBody @Valid CadastrarSalaDto dto, UriComponentsBuilder uriBuilder) {
        Sala sala = salaService.criar(dto);
        URI uri = uriBuilder.path("/salas/{id}").buildAndExpand(sala.getId()).toUri();

        RespostaSalaDto dtoResponse = modelMapper.mapSalaToDto(sala);
        dtoResponse.add(linkTo(methodOn(UnidadeSaudeController.class).buscar(dtoResponse.getIdUnidadeSaude())).withRel("unidade_saude"));
        dtoResponse.add(linkTo(methodOn(SalaController.class).listarPorUnidade(dtoResponse.getIdUnidadeSaude())).withRel("salas_da_unidade"));

        return ResponseEntity.created(uri).body(dtoResponse);
    }

    // Listar salas por unidade de saúde
    @GetMapping("/unidade/{idUnidade}")
    @Operation(
            summary = "Listar salas por unidade de saúde",
            description = "Retorna todas as salas ativas de uma unidade específica.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de salas retornada com sucesso.", content = @Content(schema = @Schema(implementation = RespostaSalaDto.class))),
                    @ApiResponse(responseCode = "404", description = "Unidade de Saúde não encontrada.")
            }
    )
    public ResponseEntity<List<RespostaSalaDto>> listarPorUnidade(@PathVariable Long idUnidade) {
        List<Sala> salas = salaService.listarPorUnidade(idUnidade);
        List<RespostaSalaDto> dtos = salas.stream().map(modelMapper::mapSalaToDto).toList();

        dtos.forEach(dto -> {
            dto.add(linkTo(methodOn(SalaController.class).excluir(dto.getId())).withRel("excluir"));
        });

        return ResponseEntity.ok(dtos);
    }

    // Excluir sala (lógico)
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir sala (lógico)",
            description = "Marca a sala como inativa no sistema. Necessário para liberar agendas.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Sala excluída com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Sala não encontrada.")
            }
    )
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        salaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}