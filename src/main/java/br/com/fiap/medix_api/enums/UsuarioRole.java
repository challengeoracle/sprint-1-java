package br.com.fiap.medix_api.enums;

import lombok.Getter;

@Getter
public enum UsuarioRole {
    COLABORADOR("colaborador"),
    PACIENTE("paciente");

    private final String role;

    UsuarioRole(String role) {
        this.role = role;
    }

    public String getAuthority() {
        return "ROLE_" + this.role.toUpperCase();
    }

}