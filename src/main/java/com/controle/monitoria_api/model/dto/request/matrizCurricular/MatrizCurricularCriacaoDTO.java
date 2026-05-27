package com.controle.monitoria_api.model.dto.request.matrizCurricular;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "MatrizCurricularCriacaoDTO", description = "DTO para cadastro de uma nova matriz curricular")
public record MatrizCurricularCriacaoDTO(
        @NotBlank(message = "Nome é obrigatório!")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres!")
        String nome,
        @NotBlank(message = "Descrição é obrigatória!")
        @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres!")
        String descricao,
        @NotNull(message = "Curso é obrigatório!")
        Long cursoId) {
}