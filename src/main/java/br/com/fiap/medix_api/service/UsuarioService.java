package br.com.fiap.medix_api.service;

import br.com.fiap.medix_api.model.Usuario;
import br.com.fiap.medix_api.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listar(String status) {
        if ("deletados".equalsIgnoreCase(status)) {
            // Se o parâmetro for "deletados", busca os deletados (deleted = 1)
            // http://localhost:8080/api/pacientes?status=deletados
            return usuarioRepository.findAllByDeletedIs(1);
        }
        // Por padrão, ou se o parâmetro for qualquer outra coisa, busca os ativos (deleted = 0)
        return usuarioRepository.findAllByDeletedIs(0);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + id));
    }

    public void excluir(Long id) {
        Usuario usuario = this.buscarPorId(id);
        usuario.setDeleted(1);
        usuarioRepository.save(usuario);
    }
}