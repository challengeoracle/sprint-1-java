// controller/ColaboradorController.java
package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.AtualizarColaboradorDto;
import br.com.fiap.medix_api.dto.CadastrarColaboradorDto;
import br.com.fiap.medix_api.model.Colaborador;
import br.com.fiap.medix_api.service.ColaboradorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/colaboradores")
@AllArgsConstructor
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    @PostMapping
    public ResponseEntity<Colaborador> criar(@RequestBody @Valid CadastrarColaboradorDto colaboradorDto, UriComponentsBuilder uriBuilder) {
        Colaborador colaborador = colaboradorService.criar(colaboradorDto);
        URI uri = uriBuilder.path("/colaboradores/{id}").buildAndExpand(colaborador.getId()).toUri();
        return ResponseEntity.created(uri).body(colaborador);
    }

    @GetMapping
    public ResponseEntity<List<Colaborador>> listar(@RequestParam(required = false) String status) {
        List<Colaborador> colaboradores = colaboradorService.listar(status);
        return ResponseEntity.ok(colaboradores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Colaborador> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(colaboradorService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Colaborador> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarColaboradorDto colaboradorDto) {
        return ResponseEntity.ok(colaboradorService.atualizar(id, colaboradorDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        colaboradorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}