package br.com.fiap.medix_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_MEDI_USUARIO")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    // Deleted serve pra deleção lógica, já inicia como 0, que é = false
    @Column(name = "deleted")
    private Integer deleted = 0;

    @CreationTimestamp
    @Column(name = "dt_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "nm_usuario")
    private String nome;

    @Column(name = "ds_email")
    private String email;

    @Column(name = "ds_senha_hash")
    private String senha;

    @Column(name = "nr_cpf")
    private String cpf;

    @Column(name = "tp_usuario")
    private String tipoUsuario;

    // Isso aqui garante que o valor padrão seja setado antes de salvar
    @PrePersist
    public void prePersist() {
        if (deleted == null) {
            deleted = 0;
        }
    }
}