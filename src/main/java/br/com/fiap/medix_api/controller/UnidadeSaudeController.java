package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.request.AtualizarUnidadeSaudeDto;
import br.com.fiap.medix_api.dto.request.CadastrarUnidadeSaudeDto;
import br.com.fiap.medix_api.model.UnidadeSaude;
import br.com.fiap.medix_api.service.UnidadeSaudeService;
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

@RestController
@RequestMapping("/unidades")
@AllArgsConstructor
@Tag(
        name = "Unidades de Saúde",
        description = "Gerencia o cadastro, listagem, atualização e exclusão lógica de unidades de saúde."
)
public class UnidadeSaudeController {

    private final UnidadeSaudeService unidadeSaudeService;

    @Operation(
            summary = "Cadastrar unidade de saúde",
            description = """
            Cria uma nova unidade de saúde no sistema com base nos dados informados.
            Retorna o objeto criado com o respectivo ID.
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Unidade de saúde cadastrada com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UnidadeSaude.class)
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
    public ResponseEntity<UnidadeSaude> criar(
            @RequestBody @Valid CadastrarUnidadeSaudeDto unidadeDto,
            UriComponentsBuilder uriBuilder
    ) {
        UnidadeSaude unidade = unidadeSaudeService.criar(unidadeDto);
        URI uri = uriBuilder.path("/unidades/{id}").buildAndExpand(unidade.getId()).toUri();
        return ResponseEntity.created(uri).body(unidade);
    }

    @Operation(
            summary = "Listar unidades de saúde",
            description = """
            Retorna todas as unidades de saúde cadastradas no sistema.
            É possível filtrar opcionalmente pelo parâmetro `status` (ex: ativo ou inativo).
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de unidades retornada com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UnidadeSaude.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<UnidadeSaude>> listar(@RequestParam(required = false) String status) {
        List<UnidadeSaude> unidades = unidadeSaudeService.listar(status);
        return ResponseEntity.ok(unidades);
    }

    @Operation(
            summary = "Buscar unidade de saúde por ID",
            description = "Retorna os dados de uma unidade de saúde específica com base no seu ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Unidade de saúde encontrada com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UnidadeSaude.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Unidade de saúde não encontrada.",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeSaude> buscar(@PathVariable Long id) {
        UnidadeSaude unidade = unidadeSaudeService.buscarPorId(id);
        return ResponseEntity.ok(unidade);
    }

    @Operation(
            summary = "Atualizar unidade de saúde",
            description = """
            Atualiza as informações de uma unidade de saúde existente com base no ID informado.
            Todos os campos enviados substituirão os valores atuais.
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Unidade de saúde atualizada com sucesso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UnidadeSaude.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Unidade de saúde não encontrada.",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeSaude> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarUnidadeSaudeDto unidadeDto
    ) {
        UnidadeSaude unidade = unidadeSaudeService.atualizar(id, unidadeDto);
        return ResponseEntity.ok(unidade);
    }

    @Operation(
            summary = "Excluir unidade de saúde (lógico)",
            description = """
            Realiza a exclusão lógica de uma unidade de saúde, mantendo o registro no banco de dados
            e marcando-o como inativo.
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Unidade de saúde excluída com sucesso."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Unidade de saúde não encontrada.",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        unidadeSaudeService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
