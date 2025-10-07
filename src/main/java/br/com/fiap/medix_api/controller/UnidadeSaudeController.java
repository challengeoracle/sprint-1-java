package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.request.AtualizarUnidadeSaudeDto;
import br.com.fiap.medix_api.dto.request.CadastrarUnidadeSaudeDto;
import br.com.fiap.medix_api.model.UnidadeSaude;
import br.com.fiap.medix_api.service.UnidadeSaudeService;
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
public class UnidadeSaudeController {

    private final UnidadeSaudeService unidadeSaudeService;

    // Cria uma nova unidade de saúde
    @PostMapping
    public ResponseEntity<UnidadeSaude> criar(@RequestBody @Valid CadastrarUnidadeSaudeDto unidadeDto, UriComponentsBuilder uriBuilder) {
        UnidadeSaude unidade = unidadeSaudeService.criar(unidadeDto);
        URI uri = uriBuilder.path("/unidades/{id}").buildAndExpand(unidade.getId()).toUri();
        return ResponseEntity.created(uri).body(unidade);
    }

    // Lista todas as unidades de saúde, com opção de filtrar por status
    @GetMapping
    public ResponseEntity<List<UnidadeSaude>> listar(@RequestParam(required = false) String status) {
        List<UnidadeSaude> unidades = unidadeSaudeService.listar(status);
        return ResponseEntity.ok(unidades);
    }

    // Busca uma unidade de saúde por ID
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeSaude> buscar(@PathVariable Long id) {
        UnidadeSaude unidade = unidadeSaudeService.buscarPorId(id);
        return ResponseEntity.ok(unidade);
    }

    // Atualiza os dados de uma unidade de saúde
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeSaude> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarUnidadeSaudeDto unidadeDto) {
        UnidadeSaude unidade = unidadeSaudeService.atualizar(id, unidadeDto);
        return ResponseEntity.ok(unidade);
    }

    // Exclui uma unidade de saúde de forma lógica
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        unidadeSaudeService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}