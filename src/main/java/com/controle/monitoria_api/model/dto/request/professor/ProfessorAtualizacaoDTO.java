package com.controle.monitoria_api.model.dto.request.professor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(name = "ProfessorAtualizacaoDTO", description = "DTO para atualização de dados de um professor")
public record ProfessorAtualizacaoDTO(
        @NotNull(message = "ID é obrigatório!")
        Long id,
        String matricula,
        String nomeCompleto,
        @Email(message = "Email inválido!")
        String email,
        String telefone,
        Long escolaId) {
}
