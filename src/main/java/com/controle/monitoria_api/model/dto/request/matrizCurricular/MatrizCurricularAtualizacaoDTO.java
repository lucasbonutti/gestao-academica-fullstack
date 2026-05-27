package com.controle.monitoria_api.model.dto.request.matrizCurricular;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "MatrizCurricularAtualizacaoDTO", description = "DTO para atualização de uma matriz curricular")
public record MatrizCurricularAtualizacaoDTO(
        @NotNull(message = "ID é obrigatório!")
        Long id,
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres!")
        String nome,
        @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres!")
        String descricao,
        Long cursoId) {
}