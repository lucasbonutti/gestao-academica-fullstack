package com.controle.monitoria_api.model.dto.request.disciplina;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(name = "DisciplinaAtualizacaoDTO", description = "DTO para atualização de dados de uma disciplina")
public record DisciplinaAtualizacaoDTO(
        @NotNull(message = "ID é obrigatório!")
        Long id,
        @Size(max = 20, message = "Sigla deve ter no máximo 20 caracteres")
        String sigla,
        @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
        String descricao,
        @Positive(message = "Carga horária deve ser positiva")
        Integer cargaHoraria,
        Long escolaId) {
}
