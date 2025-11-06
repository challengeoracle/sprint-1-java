package br.com.fiap.medix_api.enums;

import lombok.Getter;

@Getter
public enum TipoAgendamento {
    CONSULTA(30),    // 30 minutos
    EXAME(60),       // 60 minutos
    RETORNO(15),     // 15 minutos
    CIRURGIA(120);   // 2 horas

    private final int duracaoPadraoMinutos;

    TipoAgendamento(int duracaoPadraoMinutos) {
        this.duracaoPadraoMinutos = duracaoPadraoMinutos;
    }
}