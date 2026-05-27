package com.controle.monitoria_api.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "DadosAutenticacao", description = "DTO para credenciais de autenticação")
public record DadosAutenticacao(
        @NotBlank
        String login,
        @NotBlank
        String senha) {
}
