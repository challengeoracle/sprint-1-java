package br.com.fiap.medix_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // Os outros atributos serão implementados na Sprint 2
}
