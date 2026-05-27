package com.controle.monitoria_api.model.dto.request.relatorioMonitoria;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(name = "RelatorioMonitoriaCriacaoDTO", description = "DTO para criação de relatório de monitoria")
public record RelatorioMonitoriaCriacaoDTO(
        @NotNull(message = "ID da monitoria é obrigatório!")
        Long monitoriaId,
        @NotNull(message = "Número de alunos atendidos é obrigatório!")
        @Positive(message = "Número de alunos deve ser positivo")
        Integer numeroAlunosAtendidos,
        @Size(max = 500, message = "Ocorrências devem ter no máximo 500 caracteres!")
        String ocorrencias,
        @NotNull(message = "Parecer final é obrigatório!")
        @Size(max = 500, message = "Parecer final deve ter no máximo 500 caracteres!")
        String parecerFinal) {
}