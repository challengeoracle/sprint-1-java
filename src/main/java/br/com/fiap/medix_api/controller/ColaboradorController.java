// controller/ColaboradorController.java
package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.AtualizacaoColaboradorDto;
import br.com.fiap.medix_api.dto.CadastroColaboradorDto;
import br.com.fiap.medix_api.model.Colaborador;
import br.com.fiap.medix_api.service.ColaboradorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    @Autowired
    private ColaboradorService colaboradorService;

    @PostMapping
    @Transactional
    public ResponseEntity<Colaborador> criar(@RequestBody @Valid CadastroColaboradorDto colaboradorDto, UriComponentsBuilder uriBuilder) {
        Colaborador colaborador = colaboradorService.criar(colaboradorDto);
        URI uri = uriBuilder.path("/colaboradores/{id}").buildAndExpand(colaborador.getId()).toUri();
        return ResponseEntity.created(uri).body(colaborador);
    }

    @GetMapping
    public ResponseEntity<List<Colaborador>> listar() {
        List<Colaborador> colaboradores = colaboradorService.listarTodos();
        return ResponseEntity.ok(colaboradores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Colaborador> buscar(@PathVariable Long id) {
        Colaborador colaborador = colaboradorService.buscarPorId(id);
        return ResponseEntity.ok(colaborador);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Colaborador> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoColaboradorDto colaboradorDto) {
        Colaborador colaboradorAtualizado = colaboradorService.atualizar(id, colaboradorDto);
        return ResponseEntity.ok(colaboradorAtualizado);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        colaboradorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}