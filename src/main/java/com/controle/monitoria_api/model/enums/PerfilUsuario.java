package com.controle.monitoria_api.model.enums;

public enum PerfilUsuario {
    ADMIN,
    PROFESSOR;

    public String getRole() {
        return "ROLE_" + this.name();
    }
}
