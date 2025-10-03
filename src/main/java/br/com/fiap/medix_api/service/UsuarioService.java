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

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll(); // Ajuste se precisar de filtro de 'deleted'
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