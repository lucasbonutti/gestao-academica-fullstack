package com.controle.monitoria_api.model.dto.request.professor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "ProfessorCriacaoDTO", description = "DTO para cadastro de um novo professor")
public record ProfessorCriacaoDTO(
        @NotBlank(message = "Matrícula é obrigatória!")
        @Size(max = 20, message = "Matrícula deve ter no máximo 20 caracteres")
        String matricula,
        @NotBlank(message = "Nome é obrigatório!")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String nomeCompleto,
        @NotBlank(message = "Email é obrigatório!")
        @Email(message = "Email inválido!")
        @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
        String email,
        @NotBlank(message = "Telefone é obrigatório!")
        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        String telefone,
        @NotNull(message = "Escola é obrigatória!")
        Long escolaId) {
}
