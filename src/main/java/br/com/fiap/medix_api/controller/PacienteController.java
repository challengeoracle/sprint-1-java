// controller/PacienteController.java
package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.AtualizacaoPacienteDto;
import br.com.fiap.medix_api.dto.CadastroPacienteDto;
import br.com.fiap.medix_api.model.Paciente;
import br.com.fiap.medix_api.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes") // Define o caminho base para todos os endpoints desta classe
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    // CREATE (POST /pacientes)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public Paciente criar(@RequestBody CadastroPacienteDto pacienteDto) {
        return pacienteService.criar(pacienteDto);
    }

    // READ (GET /pacientes)
    @GetMapping
    public List<Paciente> listar() {
        return pacienteService.listarTodos();
    }

    // READ (GET /pacientes/{id})
    @GetMapping("/{id}")
    public Paciente buscar(@PathVariable Long id) {
        return pacienteService.buscarPorId(id);
    }

    // UPDATE (PUT /pacientes/{id})
    @PutMapping("/{id}")
    @Transactional
    public Paciente atualizar(@PathVariable Long id, @RequestBody AtualizacaoPacienteDto pacienteDto) {
        return pacienteService.atualizar(id, pacienteDto);
    }

    // DELETE (DELETE /pacientes/{id})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 No Content, ideal para deleção
    @Transactional
    public void excluir(@PathVariable Long id) {
        pacienteService.excluir(id);
    }
}