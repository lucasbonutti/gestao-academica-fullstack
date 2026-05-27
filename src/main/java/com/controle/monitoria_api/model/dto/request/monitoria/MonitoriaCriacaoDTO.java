package com.controle.monitoria_api.model.dto.request.monitoria;

import com.controle.monitoria_api.model.enums.TipoMonitoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(name = "MonitoriaCriacaoDTO", description = "DTO para cadastro de uma nova monitoria")
public record MonitoriaCriacaoDTO(
        @NotNull(message = "Aluno é obrigatório!")
        Long alunoId,
        @NotNull(message = "Disciplina é obrigatória!")
        Long disciplinaId,
        @NotNull(message = "Professor orientador é obrigatório!")
        Long professorId,
        @NotBlank(message = "Semestre é obrigatório!")
        @Size(max = 20, message = "Semestre deve ter no máximo 20 caracteres!")
        String semestre,
        @NotNull(message = "Tipo de monitoria é obrigatório!")
        TipoMonitoria tipoMonitoria,
        @NotBlank(message = "Local é obrigatório!")
        @Size(max = 200, message = "Local deve ter no máximo 200 caracteres!")
        String local,
        @NotNull(message = "Data de início é obrigatória!")
        LocalDate dataInicio,
        @NotNull(message = "Data de fim é obrigatória!")
        @Future(message = "Data de fim deve ser futura")
        LocalDate dataFim) {
}