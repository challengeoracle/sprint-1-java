package br.com.fiap.medix_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_MEDI_UNIDADE_SAUDE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unidade_saude")
    private Long id;

    @Column(name = "deleted")
    private Integer deleted = 0;

    @CreationTimestamp
    @Column(name = "dt_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "nm_unidade", nullable = false, length = 100)
    private String nome;

    @Column(name = "nr_cnpj", nullable = false, unique = true, length = 14)
    private String cnpj;

    @Column(name = "ds_endereco", nullable = false, length = 255)
    private String endereco;

    @Column(name = "nr_telefone", length = 15)
    private String telefone;

    @Column(name = "tp_unidade", nullable = false, length = 20)
    private String tipoUnidade;
}