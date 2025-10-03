// controller/PacienteController.java
package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.AtualizacaoPacienteDto;
import br.com.fiap.medix_api.dto.CadastroPacienteDto;
import br.com.fiap.medix_api.model.Paciente;
import br.com.fiap.medix_api.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @PostMapping
    public ResponseEntity<Paciente> criar(@RequestBody @Valid CadastroPacienteDto pacienteDto, UriComponentsBuilder uriBuilder) {
        Paciente paciente = pacienteService.criar(pacienteDto);
        URI uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(paciente);
    }

    @GetMapping
    public ResponseEntity<List<Paciente>> listar(@RequestParam(required = false) String status) {
        List<Paciente> pacientes = pacienteService.listar(status);
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoPacienteDto pacienteDto) {
        return ResponseEntity.ok(pacienteService.atualizar(id, pacienteDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pacienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }


}