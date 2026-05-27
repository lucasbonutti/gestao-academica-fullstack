package com.controle.monitoria_api.model.dto.request.disciplina;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(name = "DisciplinaCriacaoDTO", description = "DTO para cadastro de uma nova disciplina")
public record DisciplinaCriacaoDTO(
        @NotBlank(message = "Sigla é obrigatória!")
        @Size(max = 20, message = "Sigla deve ter no máximo 20 caracteres")
        String sigla,
        @NotBlank(message = "Descrição é obrigatória!")
        @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
        String descricao,
        @NotNull(message = "Carga horária é obrigatória!")
        @Positive(message = "Carga horária deve ser positiva")
        Integer cargaHoraria,
        @NotNull(message = "Escola é obrigatória!")
        Long escolaId) {
}
