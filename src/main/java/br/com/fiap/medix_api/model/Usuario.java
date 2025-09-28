package br.com.fiap.medix_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_MEDI_USUARIO")
@Inheritance(strategy = InheritanceType.JOINED) // Essa anotação permite a herança
@Data
@SuperBuilder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "deleted")
    private Integer deleted = 0;

    @CreationTimestamp
    @Column(name = "dt_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "nm_usuario", nullable = false, length = 100)
    private String nome;

    @Column(name = "ds_email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "ds_senha_hash", nullable = false, length = 255)
    private String senha;

    @Column(name = "nr_cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "tp_usuario", nullable = false, length = 20)
    private String tipoUsuario;
}