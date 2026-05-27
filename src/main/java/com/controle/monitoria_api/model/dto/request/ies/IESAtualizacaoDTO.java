package com.controle.monitoria_api.model.dto.request.ies;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "IESAtualizacaoDTO", description = "DTO para atualização de uma Instituição de Ensino Superior")
public record IESAtualizacaoDTO(
        @NotNull(message = "ID é obrigatório!")
        Long id,
        String nome,
        String endereco,
        String telefone) {
}
