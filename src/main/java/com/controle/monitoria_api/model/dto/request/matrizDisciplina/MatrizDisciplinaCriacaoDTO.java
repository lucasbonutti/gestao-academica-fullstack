package com.controle.monitoria_api.model.dto.request.matrizDisciplina;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(name = "MatrizDisciplinaCriacaoDTO", description = "DTO para associar uma disciplina a uma matriz curricular com seus pré-requisitos")
public record MatrizDisciplinaCriacaoDTO(
        @NotNull(message = "Matriz é obrigatória!")
        Long matrizId,
        @NotNull(message = "Disciplina é obrigatória!")
        Long disciplinaId,
        List<Long> preRequisitosIds) {
}
