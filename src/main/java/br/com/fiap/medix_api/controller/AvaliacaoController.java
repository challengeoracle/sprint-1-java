// Local: src/main/java/br/com/fiap/medix_api/controller/AvaliacaoController.java
package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.request.AtualizarAvaliacaoDto;
import br.com.fiap.medix_api.dto.request.CadastrarAvaliacaoDto;
import br.com.fiap.medix_api.model.Avaliacao;
import br.com.fiap.medix_api.service.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
@AllArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    // --- POST /avaliacoes --- (Criar)
    @PostMapping
    public ResponseEntity<Avaliacao> receberAvaliacao(@RequestBody @Valid CadastrarAvaliacaoDto dto, UriComponentsBuilder uriBuilder) { //
        Avaliacao novaAvaliacao = avaliacaoService.registrarAvaliacao(dto);
        URI uri = uriBuilder.path("/avaliacoes/{id}").buildAndExpand(novaAvaliacao.getId()).toUri(); //
        return ResponseEntity.created(uri).body(novaAvaliacao); //
    }

    // --- GET /avaliacoes --- (Listar Todos - com filtro de status)
    @GetMapping
    public ResponseEntity<List<Avaliacao>> listar(@RequestParam(required = false) String status) { // <-- Adicionado @RequestParam
        List<Avaliacao> avaliacoes = avaliacaoService.listar(status); // <-- Passa o status para o service
        return ResponseEntity.ok(avaliacoes); //
    }

    // --- GET /avaliacoes/{id} --- (Buscar por ID ativo)
    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> buscar(@PathVariable Long id) { //
        Avaliacao avaliacao = avaliacaoService.buscarPorId(id); //
        return ResponseEntity.ok(avaliacao); //
    }

    // --- PUT /avaliacoes/{id} --- (Atualizar)
    @PutMapping("/{id}")
    public ResponseEntity<Avaliacao> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarAvaliacaoDto dto) { //
        Avaliacao avaliacaoAtualizada = avaliacaoService.atualizar(id, dto); //
        return ResponseEntity.ok(avaliacaoAtualizada); //
    }

    // --- DELETE /avaliacoes/{id} --- (Excluir Logicamente)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) { //
        avaliacaoService.excluir(id); //
        return ResponseEntity.noContent().build(); //
    }
}