package com.controle.monitoria_api.model.dto.request.escola;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "EscolaAtualizacaoDTO", description = "DTO para atualização de dados de uma escola")
public record EscolaAtualizacaoDTO(
        @NotNull(message = "ID é obrigatório!")
        Long id,
        String nome,
        String coordenador,
        Long iesId) {
}
