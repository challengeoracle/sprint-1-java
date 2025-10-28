// Local: src/main/java/br/com/fiap/medix_api/model/Avaliacao.java
package br.com.fiap.medix_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "TB_MEDI_AVALIACAO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avaliacao")
    private Long id;

    // --- NOVO: Campo para exclusão lógica ---
    @Column(name = "deleted", nullable = false)
    private Integer deleted = 0; // Padrão 0 (ativo)

    @CreationTimestamp
    @Column(name = "dt_registro", updatable = false, nullable = false)
    private LocalDateTime dataRegistro;

    @NotNull(message = "O horário da avaliação não pode ser nulo.")
    @Column(name = "hr_avaliacao", nullable = false)
    private LocalTime horarioAvaliacao;

    @NotBlank(message = "O setor não pode ser vazio.")
    @Size(max = 100)
    @Column(name = "ds_setor", nullable = false, length = 100)
    private String setor;

    @NotBlank(message = "O local não pode ser vazio.")
    @Size(max = 100)
    @Column(name = "ds_local", nullable = false, length = 100)
    private String local;

    @NotBlank(message = "A avaliação não pode ser vazia.")
    @Size(max = 20)
    @Column(name = "ds_avaliacao", nullable = false, length = 20)
    private String avaliacao;

    @PrePersist
    public void prePersist() {
        if (deleted == null) {
            deleted = 0; // Garante que seja 0 se não for definido
        }
    }
}