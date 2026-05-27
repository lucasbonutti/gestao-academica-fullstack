package com.controle.monitoria_api.model.dto.request.monitoria;

import com.controle.monitoria_api.model.enums.TipoMonitoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(name = "MonitoriaAtualizacaoDTO", description = "DTO para atualização de uma monitoria existente")
public record MonitoriaAtualizacaoDTO(
        @NotNull(message = "ID é obrigatório!")
        Long id,
        Long alunoId,
        Long disciplinaId,
        Long professorId,
        @Size(max = 20, message = "Semestre deve ter no máximo 20 caracteres!")
        String semestre,
        TipoMonitoria tipoMonitoria,
        @Size(max = 200, message = "Local deve ter no máximo 200 caracteres!")
        String local,
        LocalDate dataInicio,
        LocalDate dataFim) {
}