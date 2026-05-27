package com.controle.monitoria_api.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DadosTokenJWT", description = "DTO com o token JWT gerado após autenticação")
public record DadosTokenJWT(String token) {
}
