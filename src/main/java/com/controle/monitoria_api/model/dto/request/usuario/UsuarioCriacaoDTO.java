package com.controle.monitoria_api.model.dto.request.usuario;

import com.controle.monitoria_api.model.enums.PerfilUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "UsuarioCriacaoDTO", description = "DTO para criação de usuário no sistema")
public record UsuarioCriacaoDTO(
    @NotBlank(message = "Login é obrigatório!")
    String login,
    @NotBlank(message = "Senha é obrigatória!")
    String senha,
    @NotNull(message = "Perfil é obrigatório!")
    PerfilUsuario perfil,
    Long professorId) {
}
