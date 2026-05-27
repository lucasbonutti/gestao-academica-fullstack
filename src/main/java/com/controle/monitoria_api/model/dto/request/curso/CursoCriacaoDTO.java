package com.controle.monitoria_api.model.dto.request.curso;

import com.controle.monitoria_api.model.enums.TurnoCurso;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "CursoCriacaoDTO", description = "DTO para cadastro de um novo curso")
public record CursoCriacaoDTO(
        @NotBlank(message = "Sigla é obrigatória!")
        @Size(max = 20, message = "Sigla deve ter no máximo 20 caracteres")
        String sigla,
        @NotBlank(message = "Descrição é obrigatória!")
        @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
        String descricao,
        @NotNull(message = "Escola é obrigatória!")
        Long escolaId,
        @NotNull(message = "Turno é obrigatório!")
        TurnoCurso turno,
        @NotBlank(message = "Coordenador é obrigatório!")
        @Size(max = 100, message = "Coordenador deve ter no máximo 100 caracteres")
        String coordenador) {
}
