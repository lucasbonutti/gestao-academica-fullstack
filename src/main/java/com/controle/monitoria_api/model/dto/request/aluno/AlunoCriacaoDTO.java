package com.controle.monitoria_api.model.dto.request.aluno;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "AlunoCriacaoDTO", description = "DTO para cadastro de um novo aluno")
public record AlunoCriacaoDTO(
        @NotBlank(message = "Matrícula é obrigatória!")
        @Size(max = 20, message = "Matrícula deve ter no máximo 20 caracteres!")
        String matricula,
        @NotBlank(message = "Nome completo é obrigatório!")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres!")
        String nomeCompleto) {
}