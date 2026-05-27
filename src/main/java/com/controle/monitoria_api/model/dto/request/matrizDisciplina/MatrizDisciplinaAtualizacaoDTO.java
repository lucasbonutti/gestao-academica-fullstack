package com.controle.monitoria_api.model.dto.request.matrizDisciplina;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(name = "MatrizDisciplinaAtualizacaoDTO", description = "DTO para atualizar a associação entre disciplina e matriz curricular")
public record MatrizDisciplinaAtualizacaoDTO(
        @NotNull(message = "ID é obrigatório!")
        Long id,
        Long matrizId,
        Long disciplinaId,
        List<Long> preRequisitosIds) {
}
