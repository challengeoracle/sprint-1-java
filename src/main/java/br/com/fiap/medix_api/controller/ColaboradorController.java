// controller/ColaboradorController.java
package br.com.fiap.medix_api.controller;

import br.com.fiap.medix_api.dto.AtualizacaoColaboradorDto;
import br.com.fiap.medix_api.dto.CadastroColaboradorDto;
import br.com.fiap.medix_api.model.Colaborador;
import br.com.fiap.medix_api.service.ColaboradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colaboradores") // Define o caminho base para todos os endpoints
public class ColaboradorController {

    @Autowired
    private ColaboradorService colaboradorService;

    // CREATE (POST /colaboradores)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public Colaborador criar(@RequestBody CadastroColaboradorDto colaboradorDto) {
        return colaboradorService.criar(colaboradorDto);
    }

    // READ (GET /colaboradores)
    @GetMapping
    public List<Colaborador> listar() {
        return colaboradorService.listarTodos();
    }

    // READ (GET /colaboradores/{id})
    @GetMapping("/{id}")
    public Colaborador buscar(@PathVariable Long id) {
        return colaboradorService.buscarPorId(id);
    }

    // UPDATE (PUT /colaboradores/{id})
    @PutMapping("/{id}")
    @Transactional
    public Colaborador atualizar(@PathVariable Long id, @RequestBody AtualizacaoColaboradorDto colaboradorDto) {
        return colaboradorService.atualizar(id, colaboradorDto);
    }

    // DELETE (DELETE /colaboradores/{id})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void excluir(@PathVariable Long id) {
        colaboradorService.excluir(id);
    }
}